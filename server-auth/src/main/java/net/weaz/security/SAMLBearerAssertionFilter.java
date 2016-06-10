package net.weaz.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml.SAMLProcessingFilter;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SAMLBearerAssertionFilter extends AbstractAuthenticationProcessingFilter {

    private SAMLProcessingFilter samlProcessingFilter;

    public SAMLBearerAssertionFilter(SAMLProcessingFilter samlProcessingFilter) {
        super(new AntPathRequestMatcher("/login", "POST"));
        this.samlProcessingFilter = samlProcessingFilter;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        return null;
    }
}
