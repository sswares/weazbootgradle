package net.weaz.auth.security.remote;

import net.weaz.auth.data.models.AuthCustomUser;
import net.weaz.auth.data.repositories.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.rcp.RemoteAuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
public class CustomRemoteAuthenticationManager implements RemoteAuthenticationManager {

    private CustomUserRepository customUserRepository;
    private SampleRemoteUserConfigurationProperties sampleRemoteUserConfigurationProperties;

    @Autowired
    public CustomRemoteAuthenticationManager(CustomUserRepository customUserRepository,
                                             SampleRemoteUserConfigurationProperties sampleRemoteUserConfigurationProperties) {
        this.customUserRepository = customUserRepository;
        this.sampleRemoteUserConfigurationProperties = sampleRemoteUserConfigurationProperties;
    }

    @Override
    public Collection<? extends GrantedAuthority> attemptAuthentication(String username, String password) {
        if (username.equals(sampleRemoteUserConfigurationProperties.getUsername())
            && password.equals(sampleRemoteUserConfigurationProperties.getPassword())) {
            AuthCustomUser userInRepo = customUserRepository.findByUsername(username);

            if (userInRepo == null) {
                userInRepo = new AuthCustomUser();
                userInRepo.setUsername(username);
                userInRepo.setFirstName(sampleRemoteUserConfigurationProperties.getFirstName());
                userInRepo.setLastName(sampleRemoteUserConfigurationProperties.getLastName());
                userInRepo.setPassword(password);
                userInRepo.setFavoriteCat(sampleRemoteUserConfigurationProperties.getFavoriteCat());
                customUserRepository.save(userInRepo);
            }

            return Collections.singletonList(sampleRemoteUserConfigurationProperties.getRole());
        }

        throw new BadCredentialsException("Could not authenticate user: " + username + " with password: " + password);
    }
}