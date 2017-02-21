package net.weaz.auth.security.remote;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "sample.remote.user")
@Validated
public class SampleRemoteUserConfigurationProperties {

    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String favoriteCat;
    @NotNull
    private SimpleGrantedAuthority role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFavoriteCat() {
        return favoriteCat;
    }

    public void setFavoriteCat(String favoriteCat) {
        this.favoriteCat = favoriteCat;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public SimpleGrantedAuthority getRole() {
        return role;
    }

    public void setRole(SimpleGrantedAuthority role) {
        this.role = role;
    }
}
