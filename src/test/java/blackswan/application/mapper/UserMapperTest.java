package blackswan.application.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import blackswan.application.user.UserDto;
import blackswan.domain.model.user.User;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for UserMapper interface.
 *
 * @author David Molnar
 */
public class UserMapperTest {

  private static final String USER_NAME = "userName";
  private UserMapper userMapper;

  @Before
  public void setup() {
    userMapper = new UserMapperImpl();
  }

  @Test
  public void testUserToUserDto_NullInput() {
    assertNull(userMapper.userToUserDto(null));
  }

  @Test
  public void testUserToUserDto_Successful() {
    User user = createUser();
    UserDto userDto = userMapper.userToUserDto(user);

    assertEquals(user.getUserName(), userDto.getUserName());
    assertEquals(user.getFirstName(), userDto.getFirstName());
    assertEquals(user.getLastName(), userDto.getLastName());
    assertNull(userDto.getId());
  }

  @Test
  public void testUserDtoToUser_NullInput() {
    assertNull(userMapper.userDtoToUser(null));
  }

  @Test
  public void testUserDtoToUser_Successful() {
    UserDto userDto = createUserDto();
    User user = userMapper.userDtoToUser(userDto);

    assertEquals(userDto.getUserName(), user.getUserName());
    assertEquals(userDto.getFirstName(), user.getFirstName());
    assertEquals(userDto.getLastName(), user.getLastName());
    assertNull(user.getId());
  }

  @Test
  public void testCreateUpdatedUser_NullInputs() {
    assertNull(userMapper.createUpdatedUser(null, null));
  }

  @Test
  public void testCreateUpdatedUser_NullUserDtoInput() {
    User user = userMapper.createUpdatedUser(null, USER_NAME);

    assertNotNull(user);
    assertEquals(USER_NAME, user.getUserName());
    assertNull(user.getFirstName());
    assertNull(user.getLastName());
  }

  @Test
  public void testCreateUpdatedUser_NullUserNameInput() {
    UserDto userDto = createUserDto();
    User user = null;

    try {
      user = userMapper.createUpdatedUser(userDto, null);
    } catch (Exception e) {
      assertTrue(e.getMessage().contains(USER_NAME));
    }

    assertNull(user);
  }

  @Test
  public void testCreateUpdatedUser_Successful() {
    UserDto userDto = createUserDto();
    User user = userMapper.createUpdatedUser(userDto, USER_NAME);

    assertEquals(USER_NAME, user.getUserName());
    assertEquals(userDto.getFirstName(), user.getFirstName());
    assertEquals(userDto.getLastName(), user.getLastName());
    assertNull(user.getId());
  }

  private User createUser() {
    return User.builder()
        .userName(USER_NAME)
        .firstName("firstName")
        .lastName("lastName")
        .build();
  }

  private UserDto createUserDto() {
    return UserDto.builder()
        .userName("dtoUserName")
        .firstName("dtoFirstName")
        .lastName("dtoLastName")
        .build();
  }
}
