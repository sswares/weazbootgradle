package net.weaz.security.remote;

import net.weaz.data.models.AuthCustomUser;
import net.weaz.data.repositories.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.rcp.RemoteAuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
public class CustomRemoteAuthenticationManager implements RemoteAuthenticationManager {

    private CustomUserRepository customUserRepository;

    @Autowired
    public CustomRemoteAuthenticationManager(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }

    @Override
    public Collection<? extends GrantedAuthority> attemptAuthentication(String username, String password) {
        if (username.equalsIgnoreCase("user") && password.equalsIgnoreCase("password")) {
            AuthCustomUser userInRepo = customUserRepository.findByUsername(username);

            if (userInRepo == null) {
                userInRepo = new AuthCustomUser();
            }

            userInRepo.setUsername(username);
            userInRepo.setFirstName("User");
            userInRepo.setLastName("Remote");
            userInRepo.setPassword("password");
            userInRepo.setFavoriteCat("Tippy");
            customUserRepository.save(userInRepo);

            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        throw new BadCredentialsException("Could not authenticate user: " + username + " with password " + password);
    }
}