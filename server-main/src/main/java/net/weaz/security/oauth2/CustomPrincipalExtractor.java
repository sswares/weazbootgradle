package net.weaz.security.oauth2;

import net.weaz.security.models.CustomMainUser;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CustomPrincipalExtractor implements PrincipalExtractor {

    private final FixedAuthoritiesExtractor authoritiesExtractor;

    public CustomPrincipalExtractor() {
        authoritiesExtractor = new FixedAuthoritiesExtractor();
    }

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        List<GrantedAuthority> authoritiesList = authoritiesExtractor.extractAuthorities(map);
        String favoriteCat = (String) map.get("favoriteCat");

        return new CustomMainUser(username, password, authoritiesList, favoriteCat);
    }
}