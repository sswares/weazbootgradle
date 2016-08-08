package net.weaz.data.repositories;

import net.weaz.data.models.CustomClient;
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
public class CustomClientRepositoryTest {

    public static final String TEST_CLIENT_NAME = "clientName";
    public static final String TEST_CLIENT_SECRET = "clientSecret";
    public static final String TEST_CLIENT_SCOPES = "clientScope";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomClientRepository subject;

    private CustomClient testClient;

    @Before
    public void setUp() throws Exception {
        testClient = new CustomClient(TEST_CLIENT_NAME, TEST_CLIENT_SECRET, TEST_CLIENT_SCOPES);
    }

    @Test
    public void findByClientName_findsUserByClientName() throws Exception {
        this.entityManager.persist(testClient);

        CustomClient result = subject.findByClientName(TEST_CLIENT_NAME);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getClientName()).isEqualTo(TEST_CLIENT_NAME);
        assertThat(result.getClientSecret()).isEqualTo(TEST_CLIENT_SECRET);
        assertThat(result.getScopes()).isEqualTo(TEST_CLIENT_SCOPES);
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_withoutClientName_throwsException() {
        testClient.setClientName(null);
        subject.save(testClient);
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_withoutClientSecretName_throwsException() {
        testClient.setClientSecret(null);
        subject.save(testClient);
    }

    @Test(expected = ConstraintViolationException.class)
    public void save_withoutScopes_throwsException() {
        testClient.setScopes(null);
        subject.save(testClient);
    }
}