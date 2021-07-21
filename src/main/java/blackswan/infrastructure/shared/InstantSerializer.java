package blackswan.infrastructure.shared;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Serializer class for Instant type.
 *
 * @author David Molnar
 */
public class InstantSerializer extends JsonSerializer<Instant> {

  @Override
  public void serialize(Instant instant, JsonGenerator generator, SerializerProvider provider) throws IOException {
    if (instant != null) {
      generator.writeString(Timestamp.from(instant).toString());
    }
  }

}