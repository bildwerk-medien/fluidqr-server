package de.bildwerkmedien.fluidqr.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Fluid Qr Server.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final ApplicationProperties.Url url = new ApplicationProperties.Url();

    public ApplicationProperties() {
    }

//    custom fields ### start ###
    public ApplicationProperties.Url getUrl(){
        return this.url;
    }

    public static class Url {

        private String base="localhost:8080";
        private String protocol="http://";

        public Url() {
        }

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }
    }

//    custom fields ### end ###
}
