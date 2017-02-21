package net.weaz.main.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;

import java.util.Map;

public class CustomPrincipalExtractor implements PrincipalExtractor {

    private FixedAuthoritiesExtractor fixedAuthoritiesExtractor;

    public CustomPrincipalExtractor(FixedAuthoritiesExtractor fixedAuthoritiesExtractor) {
        this.fixedAuthoritiesExtractor = fixedAuthoritiesExtractor;
    }

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        String username = (String) map.get("username");
        String favoriteCat = (String) map.get("favoriteCat");

        return new CustomMainUser(username, fixedAuthoritiesExtractor.extractAuthorities(map), favoriteCat);
    }
}