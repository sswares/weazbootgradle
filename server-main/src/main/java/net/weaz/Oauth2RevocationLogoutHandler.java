package net.weaz;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Oauth2RevocationLogoutHandler implements LogoutHandler {

    private OAuth2ClientContext clientContext;

    public Oauth2RevocationLogoutHandler(OAuth2ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //TODO: perhaps implement revocation here.  I don't know, undecided.  make a request to the endpoint, single logout...
    }
}
