package net.weaz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;

@SpringBootApplication
@EnableZuulProxy
@EnableOAuth2Sso
@SuppressWarnings("SpringJavaAutowiringInspection")
public class MainApplication extends WebSecurityConfigurerAdapter {

    private final ResourceServerTokenServices tokenServices;

    private final OAuth2ClientContext clientContext;

    @Autowired
    public MainApplication(ResourceServerTokenServices tokenServices, OAuth2ClientContext clientContext) {
        this.tokenServices = tokenServices;
        this.clientContext = clientContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.logout().addLogoutHandler(new Oauth2RevocationLogoutHandler(clientContext)).logoutSuccessUrl("/").permitAll()
                .and().antMatcher("/**").authorizeRequests()
                .antMatchers("/partials/**", "/", "/login").permitAll()
                .anyRequest().authenticated().and().csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .addFilterBefore(new BearerTokenAccessFilter(tokenServices, clientContext), CsrfFilter.class);
    }
}
