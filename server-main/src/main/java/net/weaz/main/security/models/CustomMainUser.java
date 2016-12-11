package net.weaz.main.security.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Objects;

public class CustomMainUser extends User {

    private String favoriteCat;

    public CustomMainUser(String username, Collection<? extends GrantedAuthority> authorities, String favoriteCat) {
        super(username, "noneshallpass", authorities);
        this.favoriteCat = favoriteCat;
    }

    public String getFavoriteCat() {
        return favoriteCat;
    }

    public void setFavoriteCat(String favoriteCat) {
        this.favoriteCat = favoriteCat;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)) {
            return false;
        }
        CustomMainUser that = (CustomMainUser) object;
        return Objects.equals(favoriteCat, that.favoriteCat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), favoriteCat);
    }
}