package net.weaz.auth.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {

    @Autowired
    public CustomJwtAccessTokenConverter(CustomUserAuthenticationConverter userAuthenticationConverter) {
        super();
        this.setKeyPair(
                new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "foobar".toCharArray())
                        .getKeyPair("test")
        );
        ((DefaultAccessTokenConverter) this.getAccessTokenConverter()).setUserTokenConverter(userAuthenticationConverter);
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
        customAccessToken.setAdditionalInformation(new LinkedHashMap<>(accessToken.getAdditionalInformation()));
        return super.enhance(customAccessToken, authentication);
    }
}