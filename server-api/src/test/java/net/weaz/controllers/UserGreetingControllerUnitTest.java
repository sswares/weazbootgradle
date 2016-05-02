package net.weaz.controllers;

import net.weaz.test.support.categories.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.security.Principal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Category(UnitTest.class)
public class UserGreetingControllerUnitTest {

    private UserGreetingController subject;

    @Before
    public void setUp() throws Exception {
        subject = new UserGreetingController();
    }

    @Test
    public void rootController_returnsIndex() throws Exception {
        Principal principal = mock(Principal.class);

        when(principal.getName()).thenReturn("Jonah");
        UserGreetingController.GreetingPresenter returnValue = subject.hello(principal);

        assertThat(returnValue.getMessage(), is(equalTo("Hello Jonah!")));
    }
}