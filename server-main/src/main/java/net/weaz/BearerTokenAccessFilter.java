package net.weaz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetailsSource;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class BearerTokenAccessFilter extends GenericFilterBean {

    private static Logger logger = LoggerFactory.getLogger(BearerTokenAccessFilter.class);
    private final BearerTokenExtractor bearerTokenExtractor;
    private ResourceServerTokenServices resourceServerTokenServices;
    private OAuth2ClientContext clientContext;
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new OAuth2AuthenticationDetailsSource();

    public BearerTokenAccessFilter(ResourceServerTokenServices resourceServerTokenServices,
                                   OAuth2ClientContext clientContext) {

        this.resourceServerTokenServices = resourceServerTokenServices;
        this.clientContext = clientContext;
        this.bearerTokenExtractor = new BearerTokenExtractor();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Authentication bearerTokenAuthentication = bearerTokenExtractor.extract(httpServletRequest);

        String bearerToken = null;
        if (bearerTokenAuthentication != null
                && bearerTokenAuthentication.getPrincipal() != null) {
            bearerToken = String.valueOf(bearerTokenAuthentication.getPrincipal());
        }
        OAuth2Authentication authentication = null;

        if (!isBlank(bearerToken)) {
            try {
                OAuth2AccessToken accessToken = resourceServerTokenServices.readAccessToken(bearerToken);

                clientContext.setAccessToken(resourceServerTokenServices.readAccessToken(bearerToken));
                authentication = resourceServerTokenServices.loadAuthentication(accessToken.getValue());

                if (authenticationDetailsSource != null) {
                    request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, accessToken.getValue());
                    request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, accessToken.getTokenType());
                    authentication.setDetails(authenticationDetailsSource.buildDetails(httpServletRequest));
                }
            } catch (RuntimeException e) {
                logger.warn("Exception thrown extracting access token from bearer token.  Continuing with chain.", e);
            }
        }

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}