package blackswan.infrastructure.shared;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Boolean to String converter class.
 *
 * @author David Molnar
 */
@Converter
public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {

  @Override
  public String convertToDatabaseColumn(Boolean value) {

    return (value != null && value) ? "Y" : "N";
  }

  @Override
  public Boolean convertToEntityAttribute(String value) {

    return "Y".equals(value);
  }
}
