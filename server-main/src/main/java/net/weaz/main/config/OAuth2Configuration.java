package net.weaz.main.config;

import net.weaz.main.security.CustomPrincipalExtractor;
import net.weaz.main.security.CustomUserInfoTokenServices;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
public class OAuth2Configuration {

    @Bean
    public ResourceServerTokenServices userInfoTokenServices(ResourceServerProperties resourceServerProperties,
                                                             CustomPrincipalExtractor customPrincipalExtractor) {
        CustomUserInfoTokenServices userInfoTokenServices = new CustomUserInfoTokenServices(
                resourceServerProperties.getUserInfoUri(), resourceServerProperties.getClientId(), customPrincipalExtractor);
        userInfoTokenServices.setTokenType(resourceServerProperties.getTokenType());
        return userInfoTokenServices;
    }

    @Bean
    public CustomPrincipalExtractor customPrincipalExtractor(FixedAuthoritiesExtractor fixedAuthoritiesExtractor) {
        return new CustomPrincipalExtractor(fixedAuthoritiesExtractor);
    }

    @Bean
    public FixedAuthoritiesExtractor fixedAuthoritiesExtractor() {
        return new FixedAuthoritiesExtractor();
    }
}
