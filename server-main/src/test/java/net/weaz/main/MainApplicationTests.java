package net.weaz.main;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MainApplicationTests {

    @Value("${security.oauth2.client.userAuthorizationUri}")
    private String authorizeUri;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void root_isUnprotected() {
        ResponseEntity<String> response = template.getForEntity("/", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void user_isProtected() {
        ResponseEntity<String> response = template.getForEntity("/user", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getLocation().toString()).endsWith("/login");
    }

    @Test
    public void resource_isProtected() {
        ResponseEntity<String> response = template.getForEntity("/resource", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getLocation().toString()).endsWith("/login");
    }

    @Test
    public void login_redirectsToAuthorizationUrl() {
        ResponseEntity<String> response = template.getForEntity("/login", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);

        assertThat(response.getHeaders().getFirst("Location")).startsWith(authorizeUri);
    }
}
