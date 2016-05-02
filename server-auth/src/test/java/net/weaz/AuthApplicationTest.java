package net.weaz;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AuthApplication.class)
@WebIntegrationTest(randomPort = true)
@Category(net.weaz.test.support.categories.IntegrationTest.class)
public class AuthApplicationTest {

    @Value("${local.server.port}")
    private int port;

    private RestTemplate template = new TestRestTemplate();
    private String basePath;

    @Before
    public void setUp() throws Exception {
        basePath = "http://localhost:" + port + "/auth/";
    }

    @Test
    public void root_requiresAuthentication() {
        ResponseEntity<String> response = template.getForEntity(basePath, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        String auth = response.getHeaders().getFirst("WWW-Authenticate");
        assertThat(auth).startsWith("Bearer realm=\"");
    }

    @Test
    public void user_requiresAuthentication() {
        ResponseEntity<String> response = template.getForEntity(basePath + "user", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        String auth = response.getHeaders().getFirst("WWW-Authenticate");
        assertThat(auth).startsWith("Bearer realm=\"");
    }

    @Test
    public void authorizationRedirects() {
        ResponseEntity<String> response = template.getForEntity(basePath + "oauth/authorize", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);

        String location = response.getHeaders().getFirst("Location");
        assertThat(location).isEqualTo(basePath + "login");
    }

    @Test
    public void loginSucceeds() {
        ResponseEntity<String> response = template.getForEntity(basePath + "login", String.class);
        String csrf = getCsrf(response.getBody());

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.set("username", "user");
        form.set("password", "password");
        form.set("_csrf", csrf);

        HttpHeaders headers = new HttpHeaders();
        headers.put("COOKIE", response.getHeaders().get("Set-Cookie"));
        RequestEntity<MultiValueMap<String, String>> request =
                new RequestEntity<>(form, headers, HttpMethod.POST, URI.create(basePath + "login"));
        ResponseEntity<Void> location = template.exchange(request, Void.class);

        assertThat(location.getHeaders().getFirst("Location")).isEqualTo(basePath);
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
