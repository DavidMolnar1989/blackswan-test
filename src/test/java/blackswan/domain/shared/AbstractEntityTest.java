package blackswan.domain.shared;

import static org.junit.Assert.assertEquals;

import blackswan.domain.model.user.User;
import java.time.Instant;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Test class representing AbstractEntity class behaviour.
 *
 * @author David Molnar
 */
public class AbstractEntityTest {

  @Test
  public void testGetterValues() {
    User user = User.builder().userName("userName").build();
    Instant sysdate = Instant.now();
    ReflectionTestUtils.setField(user, "createdDate", sysdate);
    ReflectionTestUtils.setField(user, "lastModifiedDate", sysdate);
    ReflectionTestUtils.setField(user, "version", 1L);

    assertEquals(sysdate, user.getCreatedDate());
    assertEquals(sysdate, user.getLastModifiedDate());
    assertEquals(1L, user.getVersion());
  }
}
