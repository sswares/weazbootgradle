package net.weaz.security.remote;

import org.springframework.security.authentication.rcp.RemoteAuthenticationException;
import org.springframework.security.authentication.rcp.RemoteAuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
public class CustomRemoteAuthenticationManager implements RemoteAuthenticationManager {

    @Override
    public Collection<? extends GrantedAuthority> attemptAuthentication(String username, String password) throws RemoteAuthenticationException {
        switch (username) {
            case "user":
                if (password.equalsIgnoreCase("password")) {
                    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                }
                break;
            case "loser":
                if (password.equalsIgnoreCase("password")) {
                    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_LOSER"));
                }
                break;
            default:
                break;
        }

        throw new RemoteAuthenticationException(
                "Could not authenticate user: " + username + " with password" + password);
    }
}