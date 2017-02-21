package net.weaz.main.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;

public class CustomUserInfoTokenServices extends UserInfoTokenServices {

    public CustomUserInfoTokenServices(String userInfoEndpointUrl, String clientId, PrincipalExtractor principalExtractor) {
        super(userInfoEndpointUrl, clientId);
        setPrincipalExtractor(principalExtractor);
    }
}