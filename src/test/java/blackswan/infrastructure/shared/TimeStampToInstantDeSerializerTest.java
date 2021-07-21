package blackswan.infrastructure.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Test class for TimeStampToInstantDeSerializer class.
 *
 * @author David Molnar
 */
@RunWith(MockitoJUnitRunner.class)
public class TimeStampToInstantDeSerializerTest {

  private TimeStampToInstantDeSerializer testedObject;

  @Mock
  private JsonParser parser;

  @Before
  public void setUp() {
    testedObject = new TimeStampToInstantDeSerializer();
  }

  @Test
  public void test_null() throws IOException {
    when(parser.getText()).thenReturn(null);
    assertNull(testedObject.deserialize(parser, null));
  }

  @Test
  public void deserialize_valid() throws IOException {
    String timeStamp = "2020-01-14 23:00:00.0";
    when(parser.getText()).thenReturn(timeStamp);

    Instant result = testedObject.deserialize(parser, null);

    assertEquals(timeStamp, Timestamp.from(result).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void deserialize_invalid() throws IOException {
    when(parser.getText()).thenReturn("2020-01-14T00:00:00Z");
    testedObject.deserialize(parser, null);
  }
}
