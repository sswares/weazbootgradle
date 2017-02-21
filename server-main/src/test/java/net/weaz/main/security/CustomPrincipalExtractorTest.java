package net.weaz.main.security;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomPrincipalExtractorTest {

    private CustomPrincipalExtractor subject;

    @Mock
    private FixedAuthoritiesExtractor fixedAuthoritiesExtractor;
    private List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(fixedAuthoritiesExtractor.extractAuthorities(any())).thenReturn(authorities);
        subject = new CustomPrincipalExtractor(fixedAuthoritiesExtractor);
    }

    @Test
    public void extractPrincipal_givenValidMap_returnsExpectedCustomMainUser() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("username", "user111");
        map.put("favoriteCat", "Poogle");

        CustomMainUser expectedUser = new CustomMainUser("user111", authorities, "Poogle");
        assertThat(subject.extractPrincipal(map)).isEqualToComparingFieldByFieldRecursively(expectedUser);

        verify(fixedAuthoritiesExtractor).extractAuthorities(map);
    }
}