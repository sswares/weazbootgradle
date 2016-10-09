package net.weaz.main.config;

import net.weaz.main.security.oauth2.CustomPrincipalExtractor;
import net.weaz.main.security.oauth2.CustomUserInfoTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableOAuth2Sso
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ResourceServerProperties sso;
    private CustomPrincipalExtractor customPrincipalExtractor;

    @Autowired
    public SecurityConfiguration(ResourceServerProperties sso, CustomPrincipalExtractor customPrincipalExtractor) {
        this.sso = sso;
        this.customPrincipalExtractor = customPrincipalExtractor;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**").authorizeRequests()
                .antMatchers("/partials/**", "/", "/login").permitAll()
                .anyRequest().authenticated().and().csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Bean
    public ResourceServerTokenServices userInfoTokenServices() {
        CustomUserInfoTokenServices userInfoTokenServices = new CustomUserInfoTokenServices(
                sso.getUserInfoUri(), sso.getClientId(), customPrincipalExtractor);
        userInfoTokenServices.setTokenType(sso.getTokenType());
        return userInfoTokenServices;
    }
}