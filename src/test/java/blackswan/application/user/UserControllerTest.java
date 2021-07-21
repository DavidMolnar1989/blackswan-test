package blackswan.application.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test class for UserController class.
 *
 * @author David Molnar
 */
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

  private static final Long USER_ID = 1L;

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  @Test
  public void testGetUserSuccessfully() {
    when(userService.getUser(USER_ID)).thenReturn(Optional.of(createUserDto("getUser")));
    assertEquals(HttpStatus.OK, userController.getUser(USER_ID).getStatusCode());
  }

  @Test
  public void testGetUserNotFound() {
    when(userService.getUser(USER_ID)).thenReturn(Optional.empty());
    assertEquals(HttpStatus.NOT_FOUND, userController.getUser(USER_ID).getStatusCode());
  }

  @Test
  public void testListUsersSuccessfully() {
    when(userService.listUsers()).thenReturn(new ArrayList<>() {{
      add(createUserDto("firstUser"));
      add(createUserDto("secondUser"));
    }});

    ResponseEntity<List<UserDto>> resultList = userController.listUsers();
    assertEquals(HttpStatus.OK, resultList.getStatusCode());
    assertNotNull(resultList.getBody());
    assertEquals(2, resultList.getBody().size());
  }

  @Test
  public void testListUsersNotFound() {
    when(userService.listUsers()).thenReturn(new ArrayList<>());
    assertEquals(HttpStatus.NOT_FOUND, userController.listUsers().getStatusCode());
  }

  @Test
  public void testCreateUserSuccessfully() {
    UserDto userDto = createUserDto("createUser");
    when(userService.createUser(userDto)).thenReturn(Optional.of(userDto));
    assertEquals(HttpStatus.OK, userController.createUser(userDto).getStatusCode());
  }

  @Test
  public void testCreateUserNotFound() {
    UserDto userDto = createUserDto("createUser");
    when(userService.createUser(userDto)).thenReturn(Optional.empty());
    assertEquals(HttpStatus.NOT_FOUND, userController.createUser(userDto).getStatusCode());
  }

  @Test
  public void testUpdateUserSuccessfully() {
    UserDto userDto = createUserDto("updateUser");
    when(userService.updateUser(USER_ID, userDto)).thenReturn(Optional.of(userDto));
    assertEquals(HttpStatus.OK, userController.updateUser(USER_ID, userDto).getStatusCode());
  }

  @Test
  public void testUpdateUserNotFound() {
    UserDto userDto = createUserDto("updateUser");
    when(userService.updateUser(USER_ID, userDto)).thenReturn(Optional.empty());
    assertEquals(HttpStatus.NOT_FOUND, userController.updateUser(USER_ID, userDto).getStatusCode());
  }

  private UserDto createUserDto(String userName) {
    return UserDto.builder()
        .userName(userName)
        .firstName("firstName")
        .lastName("lastName")
        .build();
  }
}
