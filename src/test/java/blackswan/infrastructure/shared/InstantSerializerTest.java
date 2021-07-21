package blackswan.infrastructure.shared;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test class for InstantSerializer class.
 *
 * @author David Molnar
 */
@RunWith(MockitoJUnitRunner.class)
public class InstantSerializerTest {

  private InstantSerializer testedObject;

  @Mock
  private JsonGenerator generator;

  @Before
  public void setUp() {
    testedObject = new InstantSerializer();
  }

  @Test
  public void serialize_null() throws IOException {
    testedObject.serialize(null, generator, null);

    verifyNoMoreInteractions(generator);
  }

  @Test
  public void serialize_notNull() throws IOException {
    Instant now = Instant.now();

    testedObject.serialize(now, generator, null);

    verify(generator).writeString(Timestamp.from(now).toString());
  }
}
