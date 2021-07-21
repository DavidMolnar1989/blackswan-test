package blackswan.infrastructure.shared;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import org.apache.commons.lang3.StringUtils;

/**
 * DeSerializer class from TimeStamp to Instant.
 *
 * @author David Molnar
 */
public class TimeStampToInstantDeSerializer extends JsonDeserializer<Instant> {

  @Override
  public Instant deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException {

    if (StringUtils.isBlank(arg0.getText())) {
      return null;
    } else {
      return Timestamp.valueOf(arg0.getText()).toInstant();
    }
  }
}