package net.weaz.main.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
@ConfigurationProperties(prefix = "weazboot.oauth2.revoke")
public class WeazbootOauth2RevocationConfigurationProperties {

    /**
     * Uri to use for revoking
     */
    private URI revocationUri;

    public URI getRevocationUri() {
        return revocationUri;
    }

    public void setRevocationUri(URI revocationUri) {
        this.revocationUri = revocationUri;
    }
}
