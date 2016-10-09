package net.weaz.auth;

import net.weaz.auth.test.support.categories.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Category(IntegrationTest.class)
public class AuthApplicationTest {

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    private TestRestTemplate template;

    @Test
    public void root_requiresAuthentication() {
        ResponseEntity<String> response = template.getForEntity("/auth/", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        String auth = response.getHeaders().getFirst("WWW-Authenticate");
        assertThat(auth).startsWith("Bearer realm=\"");
    }

    @Test
    public void user_requiresAuthentication() {
        ResponseEntity<String> response = template.getForEntity("/auth/user", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        String auth = response.getHeaders().getFirst("WWW-Authenticate");
        assertThat(auth).startsWith("Bearer realm=\"");
    }

    @Test
    public void authorize_redirectsToLogin() {
        ResponseEntity<String> response = template.getForEntity("/auth/oauth/authorize", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);

        String location = response.getHeaders().getFirst("Location");
        assertThat(location).endsWith("/auth/login");
    }

    @Test
    public void login_whenGivenValidParameters_redirectsToRoot() {
        ResponseEntity<String> response = template.getForEntity("/auth/login", String.class);
        String csrf = getCsrf(response.getBody());

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.set("username", "user");
        form.set("password", "password");
        form.set("_csrf", csrf);

        HttpHeaders headers = new HttpHeaders();
        headers.put("COOKIE", response.getHeaders().get("Set-Cookie"));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);
        ResponseEntity<Void> location = template.postForEntity("/auth/login", request, Void.class);

        assertThat(location.getHeaders().getFirst("Location")).endsWith("/auth/");
    }

    private String getCsrf(String soup) {
        Matcher matcher = Pattern.compile("(?s).*name=\"_csrf\".*?value=\"([^\"]+).*")
                .matcher(soup);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }
}
