package net.weaz.main.controllers;

import net.weaz.main.security.CustomMainUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserGreetingController.class)
@RunWith(SpringRunner.class)
public class UserGreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void greeting_whenUserHasRoleUser_returnsAGreetingMessageTailoredForThem() throws Exception {
        CustomMainUser testUser = new CustomMainUser("someone", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")), "myFavoriteCat");

        mockMvc.perform(get("/api/greeting").with(user(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(equalTo("Hello someone!  Your favorite cat was myFavoriteCat, right?"))));
    }

    @Test
    public void greeting_whenUserDoesNotHaveRoleUser_returnsForbidden() throws Exception {
        CustomMainUser testUser = new CustomMainUser("someone",
                Collections.singletonList(new SimpleGrantedAuthority("NONE")), "myFavoriteCat");

        mockMvc.perform(get("/api/greeting").with(user(testUser)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void greeting_whenNoUser_throwsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/greeting"))
                .andExpect(status().isUnauthorized());
    }
}