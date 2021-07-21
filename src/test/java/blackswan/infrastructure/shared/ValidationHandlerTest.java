package blackswan.infrastructure.shared;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

/**
 * Test class for ValidationHandler class.
 *
 * @author David Molnar
 */
@RunWith(MockitoJUnitRunner.class)
public class ValidationHandlerTest {

  private ValidationHandler validationHandler;
  @Mock
  private MethodParameter parameter;
  @Mock
  private BindingResult bindingResult;
  @Mock
  private HttpHeaders headers;
  @Mock
  private WebRequest request;
  @InjectMocks
  private MethodArgumentNotValidException ex;

  @Before
  public void setup() {
    validationHandler = new ValidationHandler();
  }

  @Test
  public void noErrorsFound() {
    ResponseEntity<Object> result =
        validationHandler.handleMethodArgumentNotValid(ex, headers, HttpStatus.NOT_FOUND, request);

    Assert.assertNotNull(result.getBody());
    Assert.assertTrue(((Map<String, String>) result.getBody()).isEmpty());
    Assert.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());

  }

  @Test
  public void foundErrors() {
    List<ObjectError> errors = new ArrayList<>() {{
      add(new FieldError("object1", "userName", "cannot be null"));
      add(new FieldError("object2", "taskName", "cannot be null"));
    }};
    when(bindingResult.getAllErrors()).thenReturn(errors);
    ResponseEntity<Object> result =
        validationHandler.handleMethodArgumentNotValid(ex, headers, HttpStatus.NOT_FOUND, request);

    Assert.assertNotNull(result.getBody());
    Assert.assertEquals(errors.size(), ((Map<String, String>) result.getBody()).size());
    Assert.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }
}
