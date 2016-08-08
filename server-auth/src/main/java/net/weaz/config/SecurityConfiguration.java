package net.weaz.config;

import net.weaz.security.CustomUserDetailsService;
import net.weaz.security.remote.CustomRemoteAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(-20)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private CustomRemoteAuthenticationProvider customRemoteAuthenticationProvider;
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfiguration(CustomRemoteAuthenticationProvider customRemoteAuthenticationProvider,
                                 CustomUserDetailsService customUserDetailsService) {
        this.customRemoteAuthenticationProvider = customRemoteAuthenticationProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .formLogin().loginPage("/login").permitAll()
        .and()
            .requestMatchers().antMatchers(
                "/login",
                "/oauth/authorize",
                "/h2-console/**",
                "/health",
                "/beans"
            )
        .and().authorizeRequests().anyRequest().authenticated()
        .and().csrf().ignoringAntMatchers("/h2-console/**");
        //@formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customRemoteAuthenticationProvider);
        auth.userDetailsService(customUserDetailsService);
    }
}