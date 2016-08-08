package net.weaz.data.repositories;

import net.weaz.data.models.AuthCustomUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SuppressWarnings("SpringJavaAutowiredMembersInspection")
@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomUserRepositoryTest {

    private static final String TEST_USER_FIRSTNAME = "test";
    private static final String TEST_USER_LASTNAME = "user";
    private static final String TEST_USER_USERNAME = "testuser";
    private static final String TEST_USER_PASSWORD = "12345";
    private static final String TEST_USER_FAVORITE_CAT = "Tippy";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomUserRepository subject;

    private AuthCustomUser testUser;

    @Before
    public void setUp() throws Exception {
        testUser = new AuthCustomUser(TEST_USER_FIRSTNAME, TEST_USER_LASTNAME, TEST_USER_USERNAME, TEST_USER_PASSWORD, TEST_USER_FAVORITE_CAT);
    }

    @Test
    public void findByUsername_findsUserByUsername() throws Exception {
        this.entityManager.persist(testUser);

        AuthCustomUser result = subject.findByUsername(TEST_USER_USERNAME);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getUsername()).isEqualTo(TEST_USER_USERNAME);
        assertThat(result.getPassword()).isEqualTo(TEST_USER_PASSWORD);
        assertThat(result.getFirstName()).isEqualTo(TEST_USER_FIRSTNAME);
        assertThat(result.getLastName()).isEqualTo(TEST_USER_LASTNAME);
        assertThat(result.getFavoriteCat()).isEqualTo(TEST_USER_FAVORITE_CAT);
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_withoutFirstName_throwsException() {
        testUser.setFirstName(null);
        subject.save(testUser);
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_withoutLastName_throwsException() {
        testUser.setLastName(null);
        subject.save(testUser);
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_withoutUsername_throwsException() {
        testUser.setUsername(null);
        subject.save(testUser);
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_withoutPassword_throwsException() {
        testUser.setPassword(null);
        subject.save(testUser);
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_withoutFavoriteCat_throwsException() {
        testUser.setFirstName(null);
        subject.save(testUser);
    }
}