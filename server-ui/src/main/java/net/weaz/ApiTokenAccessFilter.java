package net.weaz;

import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ApiTokenAccessFilter extends GenericFilterBean {

    private final BearerTokenExtractor bearerTokenExtractor;
    private ResourceServerTokenServices resourceServerTokenServices;
    private OAuth2ClientContext oAuth2ClientContext;

    public ApiTokenAccessFilter(ResourceServerTokenServices resourceServerTokenServices,
                                OAuth2ClientContext oAuth2ClientContext) {

        this.resourceServerTokenServices = resourceServerTokenServices;
        this.oAuth2ClientContext = oAuth2ClientContext;
        this.bearerTokenExtractor = new BearerTokenExtractor();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String bearerToken = bearerTokenExtractor.extract((HttpServletRequest) request).getPrincipal().toString();

        if (!isBlank(bearerToken)) {
            oAuth2ClientContext.setAccessToken(resourceServerTokenServices.readAccessToken(bearerToken));
        }

        chain.doFilter(request, response);
    }
}