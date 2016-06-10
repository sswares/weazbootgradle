package net.weaz.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@ConfigurationProperties(prefix = "security.saml")
public class SAMLSecurityConfigurationProperties {

    private String protocol;
    private String hostName;
    private String basePath;
    private Resource metadataResource;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public Resource getMetadataResource() {
        return metadataResource;
    }

    public void setMetadataResource(Resource metadataResource) {
        this.metadataResource = metadataResource;
    }
}
