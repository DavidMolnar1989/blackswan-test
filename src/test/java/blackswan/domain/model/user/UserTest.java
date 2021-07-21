package blackswan.domain.model.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for User entity.
 *
 * @author David Molnar
 */
public class UserTest {

  private static final String USER_NAME = "userName";
  private static final String FIRST_NAME = "firstName";
  private static final String LAST_NAME = "lastName";
  private User.UserBuilder builder;

  @Before
  public void setup() {
    this.builder = User.builder();
    this.builder.id(1L);
    this.builder.firstName(FIRST_NAME);
    this.builder.lastName(LAST_NAME);
  }

  @Test
  public void testUserNameValidationIssue() {
    User user = null;

    try {
      user = this.builder.build();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains(USER_NAME));
    }
    assertNull(user);
  }

  @Test
  public void testCreateUser() {
    this.builder.userName(USER_NAME);
    User user = this.builder.build();

    assertNotNull(user);
    assertEquals(USER_NAME, user.getUserName());
    assertEquals(FIRST_NAME, user.getFirstName());
    assertEquals(LAST_NAME, user.getLastName());
  }

  @Test
  public void testMergeSuccessful() {
    this.builder.userName(USER_NAME);
    User user = this.builder.build();

    User otherUser = User.builder()
        .userName(USER_NAME)
        .firstName("two")
        .lastName("three")
        .build();

    user.merge(otherUser);

    assertEquals(user.getUserName(), otherUser.getUserName());
    assertEquals(user.getFirstName(), otherUser.getFirstName());
    assertEquals(user.getLastName(), otherUser.getLastName());
    assertNull(user.getId());
  }

  @Test
  public void testMergeDefaultValues() {
    this.builder.userName(USER_NAME);
    User user = this.builder.build();

    User otherUser = User.builder()
        .userName(USER_NAME)
        .firstName(null)
        .lastName(null)
        .build();

    user.merge(otherUser);

    assertEquals(USER_NAME, user.getUserName());
    assertEquals(FIRST_NAME, user.getFirstName());
    assertEquals(LAST_NAME, user.getLastName());
  }
}
