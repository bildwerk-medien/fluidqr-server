package de.bildwerkmedien.fluidqr.server.web.rest;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.LiveBroadcast;
import com.google.api.services.youtube.model.LiveBroadcastListResponse;
import de.bildwerkmedien.fluidqr.server.domain.GoogleUser;
import de.bildwerkmedien.fluidqr.server.service.GoogleUserService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class YouTubeController {

    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final GoogleUserService googleUserService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public YouTubeController(
        OAuth2AuthorizedClientManager authorizedClientManager,
        @Qualifier("googleUserServiceExtendedImpl") GoogleUserService googleUserService,
        ClientRegistrationRepository clientRegistrationRepository
    ) {
        this.authorizedClientManager = authorizedClientManager;
        this.googleUserService = googleUserService;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @RequestMapping("/login/oauth2/code/google")
    public void callbackUrl(@CookieValue(value = "guser") String gusr) {
        GoogleUser googleUser = googleUserService.findOne(Long.parseLong(gusr, 10)).orElseThrow();

        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
            .withClientRegistrationId("google")
            .principal(googleUser.getName())
            .build();

        OAuth2AuthorizedClient authorizedClient = this.authorizedClientManager.authorize(authorizeRequest);

        googleUser.setRefreshToken(authorizedClient.getRefreshToken().getTokenValue());
        googleUserService.partialUpdate(googleUser);
    }

    @RequestMapping("/api/youtube/list/{id}")
    public List<LiveBroadcast> youtubeListReq(@PathVariable(value = "id") final Long id) throws IOException {
        final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
        final JsonFactory JSON_FACTORY = new GsonFactory();

        var clientId = clientRegistrationRepository.findByRegistrationId("google").getClientId();
        var clientSecret = clientRegistrationRepository.findByRegistrationId("google").getClientSecret();

        Credential credential = new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
            .setJsonFactory(JSON_FACTORY)
            .setTransport(HTTP_TRANSPORT)
            .setTokenServerUrl(new GenericUrl("https://oauth2.googleapis.com/token"))
            .setClientAuthentication(new ClientParametersAuthentication(clientId, clientSecret))
            .build();

        GoogleUser googleUser = googleUserService.findOne(id).orElseThrow();

        credential.setRefreshToken(googleUser.getRefreshToken());

        final YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
            .setApplicationName("youtube-cmdline-listbroadcasts-sample")
            .build();

        // Create a request to list broadcasts.
        YouTube.LiveBroadcasts.List liveBroadcastRequest = youtube.liveBroadcasts().list(List.of("id", "snippet"));

        // Indicate that the API response should not filter broadcasts
        // based on their type or status.
        liveBroadcastRequest.setBroadcastType("all").setBroadcastStatus("all");

        // Execute the API request and return the list of broadcasts.
        LiveBroadcastListResponse returnedListResponse = liveBroadcastRequest.execute();
        List<LiveBroadcast> returnedList = returnedListResponse.getItems();

        return returnedList;
    }
}
