package net.weaz.auth.controllers;

import net.weaz.auth.data.models.AuthCustomUser;
import net.weaz.auth.security.userdetails.CustomUserDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void user_whenLoggedIn_returnsCurrentUser() throws Exception {
        AuthCustomUser user = new AuthCustomUser();
        user.setId(1L);
        user.setUsername("something@somewhere.com");
        user.setPassword("wub");
        user.setFavoriteCat("poogle");
        user.setFirstName("Test");
        user.setLastName("Maniac");

        CustomUserDetails userDetails = new CustomUserDetails(user);

        mockMvc.perform(get("/user").with(user(userDetails)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content()
                        .json("{\n"
                              + "  \"id\": 1,\n"
                              + "  \"username\": \"something@somewhere.com\",\n"
                              + "  \"firstName\": \"Test\",\n"
                              + "  \"lastName\": \"Maniac\",\n"
                              + "  \"favoriteCat\": \"poogle\",\n"
                              + "  \"authorities\": [\n"
                              + "    {\n"
                              + "      \"authority\": \"ROLE_USER\"\n"
                              + "    }\n"
                              + "  ]\n"
                              + "}"));
    }
}