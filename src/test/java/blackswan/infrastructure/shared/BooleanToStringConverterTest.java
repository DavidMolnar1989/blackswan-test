package blackswan.infrastructure.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for BooleanToStringConverter class.
 *
 * @author David Molnar
 */
public class BooleanToStringConverterTest {

  private BooleanToStringConverter testedObject;

  @Before
  public void setUp() {
    testedObject = new BooleanToStringConverter();
  }

  @Test
  public void convertToDatabaseColumn_null() {
    assertEquals("N", testedObject.convertToDatabaseColumn(null));
  }

  @Test
  public void convertToDatabaseColumn_false() {
    assertEquals("N", testedObject.convertToDatabaseColumn(false));
  }

  @Test
  public void convertToDatabaseColumn_true() {
    assertEquals("Y", testedObject.convertToDatabaseColumn(true));
  }

  @Test
  public void convertToEntityAttribute_null() {
    assertFalse(testedObject.convertToEntityAttribute(null));
  }

  @Test
  public void convertToEntityAttribute_notY() {
    assertFalse(testedObject.convertToEntityAttribute("N"));
    assertFalse(testedObject.convertToEntityAttribute("y"));
  }

  @Test
  public void convertToEntityAttribute_Y() {
    assertTrue(testedObject.convertToEntityAttribute("Y"));
  }
}
