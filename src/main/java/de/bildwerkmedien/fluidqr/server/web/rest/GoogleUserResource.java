package de.bildwerkmedien.fluidqr.server.web.rest;

import de.bildwerkmedien.fluidqr.server.domain.GoogleUser;
import de.bildwerkmedien.fluidqr.server.repository.GoogleUserRepository;
import de.bildwerkmedien.fluidqr.server.service.GoogleUserService;
import de.bildwerkmedien.fluidqr.server.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link de.bildwerkmedien.fluidqr.server.domain.GoogleUser}.
 */
@RestController
@RequestMapping("/api")
public class GoogleUserResource {

    private final Logger log = LoggerFactory.getLogger(GoogleUserResource.class);

    private final GoogleUserService googleUserService;

    private final GoogleUserRepository googleUserRepository;

    public GoogleUserResource(GoogleUserService googleUserService, GoogleUserRepository googleUserRepository) {
        this.googleUserService = googleUserService;
        this.googleUserRepository = googleUserRepository;
    }

    /**
     * {@code GET  /google-users} : get all the googleUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of googleUsers in body.
     */
    @GetMapping("/google-users")
    public List<GoogleUser> getAllGoogleUsers() {
        log.debug("REST request to get all GoogleUsers");
        return googleUserService.findAll();
    }

    /**
     * {@code GET  /google-users/:id} : get the "id" googleUser.
     *
     * @param id the id of the googleUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the googleUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/google-users/{id}")
    public ResponseEntity<GoogleUser> getGoogleUser(@PathVariable Long id) {
        log.debug("REST request to get GoogleUser : {}", id);
        Optional<GoogleUser> googleUser = googleUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(googleUser);
    }
}
