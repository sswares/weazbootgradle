package net.weaz.auth.security.userdetails;

import net.weaz.auth.data.models.AuthCustomUser;
import net.weaz.auth.data.repositories.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private CustomUserRepository customUserRepository;

    @Autowired
    public CustomUserDetailsService(CustomUserRepository customUserRepository) {
        this.customUserRepository = customUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthCustomUser user = customUserRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Could not load user: " + username);
        }
        return new CustomUserDetails(user);
    }
}