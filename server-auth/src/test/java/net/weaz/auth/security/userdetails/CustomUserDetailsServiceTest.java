package net.weaz.auth.security.userdetails;

import net.weaz.auth.data.models.AuthCustomUser;
import net.weaz.auth.data.repositories.CustomUserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class CustomUserDetailsServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private CustomUserDetailsService subject;
    @Mock
    private CustomUserRepository customUserRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        subject = new CustomUserDetailsService(customUserRepository);
    }

    @Test
    public void loadUserByUsername_whenRepositoryReturnsAUser_createsCorrectCustomUserDetailsObject() throws Exception {
        AuthCustomUser user = new AuthCustomUser();
        user.setId(1L);
        user.setUsername("someUser");
        user.setFavoriteCat("poogle");
        user.setFirstName("something");
        user.setLastName("else");
        user.setPassword("wubwub1");

        when(customUserRepository.findByUsername("someUser")).thenReturn(user);
        UserDetails response = subject.loadUserByUsername("someUser");

        assertThat(response.getUsername()).isEqualTo("someUser");
        assertThat(response.getAuthorities()).isNotEmpty();
        assertThat(response.isEnabled()).isTrue();
        assertThat(response.isAccountNonExpired()).isTrue();
        assertThat(response.isAccountNonLocked()).isTrue();
        assertThat(response.isCredentialsNonExpired()).isTrue();
    }

    @Test
    public void loadClientByClientId_whenClientRepositoryReturnsNoClient_throwsException() throws Exception {
        when(customUserRepository.findByUsername("someUser")).thenReturn(null);

        expectedException.expect(UsernameNotFoundException.class);
        expectedException.expectMessage("Could not load user: someUser");
        subject.loadUserByUsername("someUser");
    }
}