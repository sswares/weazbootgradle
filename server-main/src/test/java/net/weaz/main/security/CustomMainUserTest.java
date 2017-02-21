package net.weaz.main.security;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomMainUserTest {

    private CustomMainUser testUser;
    private SimpleGrantedAuthority authority;
    private List<SimpleGrantedAuthority> grantedAuthorities;

    @Before
    public void setUp() throws Exception {
        authority = new SimpleGrantedAuthority("ROLE_SOMETHING");
        grantedAuthorities = Collections.singletonList(authority);
        testUser = new CustomMainUser("username", grantedAuthorities, "Tippy");
    }

    @Test
    public void constructor_createsTheCorrectObject() throws Exception {
        assertThat(testUser.getUsername()).isEqualTo("username");
        assertThat(testUser.getFavoriteCat()).isEqualTo("Tippy");
        assertThat(testUser.getAuthorities()).containsOnly(authority);
        assertThat(testUser.getPassword()).isEqualTo("");
        assertThat(testUser.isEnabled()).isTrue();
        assertThat(testUser.isAccountNonExpired()).isTrue();
        assertThat(testUser.isAccountNonLocked()).isTrue();
        assertThat(testUser.isCredentialsNonExpired()).isTrue();
    }

    @Test
    @SuppressWarnings("all")
    public void equals_whenTheObjectsAreTheSame_returnsTrue() throws Exception {
        assertThat(testUser.equals(testUser)).isTrue();
    }

    @Test
    public void equals_whenNotTheSameClass_returnsFalse() throws Exception {
        User nonUserUser = new User("username", "", grantedAuthorities);

        assertThat(testUser.equals(nonUserUser)).isFalse();
    }

    @Test
    public void equals_whenSameClassAndDifferentUsername_returnsFalse() throws Exception {
        CustomMainUser similarCustomUser = new CustomMainUser("otherUsername", grantedAuthorities, "Tippy");

        assertThat(testUser.equals(similarCustomUser)).isFalse();
    }

    @Test
    @SuppressWarnings("all")
    public void equals_handlesNulls() throws Exception {
        assertThat(testUser.equals(null)).isFalse();
    }

    @Test
    @SuppressWarnings("all")
    public void equals_handlesCats() throws Exception {
        CustomMainUser otherUserWithDifferentCat = new CustomMainUser("username", grantedAuthorities, "NotTippy");

        assertThat(testUser.equals(otherUserWithDifferentCat)).isFalse();
    }

    @Test
    public void hashCode_isImplemented() throws Exception {
        assertThat(testUser.hashCode()).isNotZero();
    }
}