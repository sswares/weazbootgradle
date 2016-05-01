package net.weaz.controllers;

import net.weaz.test.support.categories.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@Category(UnitTest.class)
public class HelloWorldControllerUnitTest {

    private HelloWorldController subject;

    @Before
    public void setUp() throws Exception {
        subject = new HelloWorldController();
    }

    @Test
    public void rootController_returnsIndex() throws Exception {
        HelloWorldController.HelloWorldPresenter returnValue = subject.hello();

        assertThat(returnValue.getMessage(), is(equalTo("Hello World!")));
    }
}