package net.weaz.auth.security.oauth2;

import net.weaz.auth.data.models.CustomClient;
import net.weaz.auth.data.repositories.CustomClientRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class CustomClientDetailsServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private CustomClientDetailsService subject;
    @Mock
    private CustomClientRepository customClientRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new CustomClientDetailsService(customClientRepository);
    }

    @Test
    public void loadClientByClientId_whenClientRepositoryReturnsAClient_returnsExpectedObject() throws Exception {
        CustomClient customClient = new CustomClient();
        customClient.setId(1L);
        customClient.setClientName("someClient");
        customClient.setClientSecret("clientSecret");
        customClient.setScopes("this, that");

        when(customClientRepository.findByClientName("someClient")).thenReturn(customClient);

        ClientDetails result = subject.loadClientByClientId("someClient");

        assertThat(result.getClientId()).isEqualTo("someClient");
        assertThat(result.getResourceIds()).isNull();
        assertThat(result.isSecretRequired()).isTrue();
        assertThat(result.isScoped()).isFalse();
        assertThat(result.getScope()).containsExactlyInAnyOrder("this", "that");
        assertThat(result.getAuthorizedGrantTypes()).containsExactlyInAnyOrder("authorization_code", "token", "refresh_token", "password");
        assertThat(result.getRegisteredRedirectUri()).isNull();
        assertThat(result.getAuthorities()).contains(new SimpleGrantedAuthority("ROLE_USER"));
        assertThat(result.getAccessTokenValiditySeconds()).isNull();
        assertThat(result.getRefreshTokenValiditySeconds()).isNull();
        assertThat(result.isAutoApprove("")).isTrue();
        assertThat(result.getAdditionalInformation()).isNull();
    }

    @Test
    public void loadClientByClientId_whenClientRepositoryReturnsNoClient_throwsException() throws Exception {
        when(customClientRepository.findByClientName("someClient")).thenReturn(null);

        expectedException.expect(ClientRegistrationException.class);
        expectedException.expectMessage("Could not find client with clientId: someClient");
        subject.loadClientByClientId("someClient");
    }
}