package net.weaz.security.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AuthCustomUser extends User {

    private String favoriteCat;

    public AuthCustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, String favoriteCat) {
        super(username, password, authorities);
        this.favoriteCat = favoriteCat;
    }

    public String getFavoriteCat() {
        return favoriteCat;
    }

    public void setFavoriteCat(String favoriteCat) {
        this.favoriteCat = favoriteCat;
    }
}