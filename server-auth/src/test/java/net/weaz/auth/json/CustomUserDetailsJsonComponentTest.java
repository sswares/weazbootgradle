package net.weaz.auth.json;

import net.weaz.auth.data.models.AuthCustomUser;
import net.weaz.auth.security.userdetails.CustomUserDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class CustomUserDetailsJsonComponentTest {

    @Autowired
    private JacksonTester<CustomUserDetails> json;

    @Test
    public void serializeJson() throws Exception {
        AuthCustomUser authCustomUser = new AuthCustomUser();
        authCustomUser.setId(1L);
        authCustomUser.setUsername("username");
        authCustomUser.setFirstName("something");
        authCustomUser.setLastName("else");
        authCustomUser.setFavoriteCat("poogle");
        CustomUserDetails customUserDetails = new CustomUserDetails(authCustomUser);

        assertThat(this.json.write(customUserDetails)).isEqualTo("customUserDetails.json");
        assertThat(this.json.write(customUserDetails)).isEqualToJson("customUserDetails.json");
        assertThat(this.json.write(customUserDetails)).doesNotHaveJsonPathValue("password");
    }
}