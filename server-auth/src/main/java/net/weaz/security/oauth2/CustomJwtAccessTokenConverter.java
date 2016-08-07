package net.weaz.security.oauth2;

import net.weaz.security.models.AuthCustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {

    private UserDetailsService userDetailsService;

    @Autowired
    public CustomJwtAccessTokenConverter(CustomUserAuthenticationConverter userAuthenticationConverter,
                                         UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;

        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "foobar".toCharArray()).getKeyPair("test");
        this.setKeyPair(keyPair);
        ((DefaultAccessTokenConverter) this.getAccessTokenConverter()).setUserTokenConverter(userAuthenticationConverter);
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
        Map<String, Object> additionalInformation = new LinkedHashMap<>(accessToken.getAdditionalInformation());

        AuthCustomUser currentUser = (AuthCustomUser) userDetailsService.loadUserByUsername(String.valueOf(authentication.getPrincipal()));
        additionalInformation.put("favoriteCat", currentUser.getFavoriteCat());

        customAccessToken.setAdditionalInformation(additionalInformation);
        return super.enhance(customAccessToken, authentication);
    }
}