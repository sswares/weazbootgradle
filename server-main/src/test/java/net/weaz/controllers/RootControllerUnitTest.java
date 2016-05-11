package net.weaz.controllers;

import net.weaz.test.support.categories.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.assertj.core.api.Assertions.assertThat;

@Category(UnitTest.class)
public class RootControllerUnitTest {

    private RootController subject;

    @Before
    public void setUp() throws Exception {
        subject = new RootController();
    }

    @Test
    public void rootController_returnsIndex() throws Exception {
        String returnValue = subject.rootController();

        assertThat(returnValue).isEqualTo("index");
    }
}