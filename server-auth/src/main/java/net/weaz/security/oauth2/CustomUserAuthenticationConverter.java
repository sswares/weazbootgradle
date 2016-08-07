package net.weaz.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Autowired
    public CustomUserAuthenticationConverter(UserDetailsService userDetailsService) {
        this.setUserDetailsService(userDetailsService);
    }
}