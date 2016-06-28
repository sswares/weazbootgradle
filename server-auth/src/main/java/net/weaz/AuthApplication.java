package net.weaz;

import net.weaz.security.SAMLBearerAssertionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLDiscovery;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.SAMLProcessingFilter;
import org.springframework.security.saml.context.SAMLContextProvider;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.metadata.CachingMetadataManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.MetadataDisplayFilter;
import org.springframework.security.saml.metadata.MetadataGenerator;
import org.springframework.security.saml.metadata.MetadataGeneratorFilter;
import org.springframework.security.saml.processor.SAMLProcessor;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.security.KeyPair;
import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Controller
@SessionAttributes("authorizationRequest")
@EnableResourceServer
public class AuthApplication extends WebMvcConfigurerAdapter {

    private static Logger logger = LoggerFactory.getLogger(AuthApplication.class);

    @Autowired
    private AuthorizationServerTokenServices tokenServices;

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @RequestMapping("/user")
    @ResponseBody
    public Principal user(Principal user, OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken = tokenServices.getAccessToken(authentication);

        if (accessToken != null && accessToken.getAdditionalInformation() != null) {
            Map<String, Object> requestMap = accessToken.getAdditionalInformation();

            for (Map.Entry<String, Object> mapEntry : requestMap.entrySet()) {
                logger.warn("Map had this key: " + mapEntry.getKey() + " with this value: " + mapEntry.getValue());
            }
        }

        return user;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/oauth/confirm_access").setViewName("authorize");
    }

    @Configuration
    @Order(-20)
    protected static class LoginConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private SAMLProcessingFilter samlProcessingFilter;

        @Autowired
        private SAMLEntryPoint samlEntryPoint;

        @Autowired
        private SAMLContextProvider contextProvider;

        @Autowired
        private SAMLAuthenticationProvider samlAuthenticationProvider;

        @Autowired
        private SAMLProcessor samlProcessor;

        @Autowired
        private CachingMetadataManager cachingMetadataManager;

        @Autowired
        private KeyManager keyManager;

        @Autowired
        private ExtendedMetadata extendedMetadata;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .formLogin().loginPage("/login")
                    .permitAll().and().requestMatchers()
                    .antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access", "/saml/sso", "/beans")
                    .and()
                    .authorizeRequests().anyRequest().authenticated()
                    .and()
                    .csrf().ignoringAntMatchers("/saml/sso")
                    .and()
                    .addFilterBefore(metadataGeneratorFilter(samlEntryPoint, extendedMetadata), ChannelProcessingFilter.class)
                    .addFilterAfter(new SAMLBearerAssertionFilter(samlProcessingFilter), UsernamePasswordAuthenticationFilter.class)
                    .addFilterAfter(samlFilter(samlEntryPoint, contextProvider), BasicAuthenticationFilter.class)
                    .authenticationProvider(samlAuthenticationProvider)
            ;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.parentAuthenticationManager(authenticationManager);
        }

        private MetadataGeneratorFilter metadataGeneratorFilter(SAMLEntryPoint samlEntryPoint, ExtendedMetadata extendedMetadata) {
            MetadataGenerator metadataGenerator = new MetadataGenerator();

            metadataGenerator.setSamlEntryPoint(samlEntryPoint);
            metadataGenerator.setEntityBaseURL("figutethisout");
            metadataGenerator.setKeyManager(keyManager);
            metadataGenerator.setEntityId("12345");
            metadataGenerator.setIncludeDiscoveryExtension(false);
            metadataGenerator.setExtendedMetadata(extendedMetadata);

            MetadataGeneratorFilter metadataGeneratorFilter = new MetadataGeneratorFilter(metadataGenerator);
            metadataGeneratorFilter.setManager(cachingMetadataManager);
            return metadataGeneratorFilter;
        }

        private FilterChainProxy samlFilter(SAMLEntryPoint samlEntryPoint, SAMLContextProvider contextProvider) {
            List<SecurityFilterChain> chains = new ArrayList<>();
            chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/login/**"), samlEntryPoint));
            chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/metadata/**"), new MetadataDisplayFilter()));

            SAMLProcessingFilter samlProcessingFilter = new SAMLProcessingFilter();
            samlProcessingFilter.setAuthenticationManager(authenticationManager);
            samlProcessingFilter.setContextProvider(contextProvider);
            samlProcessingFilter.setSAMLProcessor(samlProcessor);

            chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/SSO/**"), samlProcessingFilter));

            SAMLDiscovery samlDiscovery = new SAMLDiscovery();
            samlDiscovery.setMetadata(cachingMetadataManager);
            samlDiscovery.setContextProvider(contextProvider);

            chains.add(new DefaultSecurityFilterChain(new AntPathRequestMatcher("/saml/discovery/**"), samlDiscovery));
            return new FilterChainProxy(chains);
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class OAuth2AuthorizationConfig extends
            AuthorizationServerConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter converter = new EnhancedJwtAccessTokenConverter();
            KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "foobar".toCharArray())
                    .getKeyPair("test");
            converter.setKeyPair(keyPair);
            return converter;
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("acme")
                    .secret("acmesecret")
                    .autoApprove(true)
                    .authorizedGrantTypes("authorization_code", "refresh_token",
                            "password").scopes("openid");
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints)
                throws Exception {
            endpoints.authenticationManager(authenticationManager).accessTokenConverter(jwtAccessTokenConverter());
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer)
                throws Exception {
            oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
        }

        private static class EnhancedJwtAccessTokenConverter extends JwtAccessTokenConverter {

            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);

                Map<String, Object> info = new LinkedHashMap<>(accessToken.getAdditionalInformation());

                info.put("something", "something else!");

                customAccessToken.setAdditionalInformation(info);
                return super.enhance(customAccessToken, authentication);
            }
        }
    }
}
