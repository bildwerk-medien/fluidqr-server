package de.bildwerkmedien.fluidqr.server.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import de.bildwerkmedien.fluidqr.server.service.MailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoOpMailConfiguration {

    private final MailService mockMailService;

    public NoOpMailConfiguration() {
        mockMailService = mock(MailService.class);
        doNothing().when(mockMailService).sendActivationEmail(any());
    }

    @Bean
    public MailService mailService() {
        return mockMailService;
    }
}
