package blackswan.application.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import blackswan.application.mapper.UserMapper;
import blackswan.domain.model.user.User;
import blackswan.domain.model.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Test class for UserService interface.
 *
 * @author David Molnar
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  private static final Long USER_ID = 1L;

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapper userMapper;
  @InjectMocks
  private UserServiceImpl userService;

  @Test
  public void testCreateUser_UserDtoIsNull() {
    Assert.assertTrue(userService.createUser(null).isEmpty());
  }

  @Test
  public void testCreateUser_UserAlreadyExists() {
    UserDto userDto = createUserDto("existingUser");
    User user = createUser("existingUser");

    when(userMapper.userDtoToUser(userDto)).thenReturn(user);
    when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.of(user));
    when(userMapper.userToUserDto(user)).thenReturn(userDto);

    Optional<UserDto> createdUserDtoOpt = userService.createUser(userDto);
    assertFalse(createdUserDtoOpt.isEmpty());
    assertEquals(createdUserDtoOpt.get().getUserName(), userDto.getUserName());
    assertEquals(createdUserDtoOpt.get().getFirstName(), userDto.getFirstName());
    assertEquals(createdUserDtoOpt.get().getLastName(), userDto.getLastName());
  }

  @Test
  public void testCreateUser_Successfully() {
    UserDto userDto = createUserDto("existingUser");
    User user = createUser("existingUser");

    when(userMapper.userDtoToUser(userDto)).thenReturn(user);
    when(userRepository.findByUserName(user.getUserName())).thenReturn(Optional.empty());
    when(userRepository.save(any(User.class))).thenAnswer((i -> i.getArguments()[0]));
    when(userMapper.userToUserDto(user)).thenReturn(userDto);

    Optional<UserDto> createdUserDtoOpt = userService.createUser(userDto);
    assertFalse(createdUserDtoOpt.isEmpty());
    assertEquals(createdUserDtoOpt.get().getUserName(), userDto.getUserName());
    assertEquals(createdUserDtoOpt.get().getFirstName(), userDto.getFirstName());
    assertEquals(createdUserDtoOpt.get().getLastName(), userDto.getLastName());
  }

  @Test
  public void testUpdateUser_CannotBeFound() {
    Assert.assertTrue(userService.updateUser(USER_ID, null).isEmpty());
    Assert.assertTrue(userService.updateUser(null, null).isEmpty());
  }

  @Test
  public void testUpdateUser_UserDtoIsNull() {
    User user = createUser("userDtoIsNull");
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    Assert.assertTrue(userService.updateUser(USER_ID, null).isEmpty());
  }

  @Test
  public void testUpdateUser_Successfully() {
    User user = createUser("updateUser");
    User updatedUser = createUser("updatedUser");
    ReflectionTestUtils.setField(updatedUser, "firstName", "merged");
    ReflectionTestUtils.setField(updatedUser, "lastName", "merged");
    UserDto userDto = createUserDto("updateUser");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(userMapper.createUpdatedUser(userDto, user.getUserName())).thenReturn(updatedUser);
    when(userMapper.userToUserDto(user)).thenReturn(userDto);

    Optional<UserDto> result = userService.updateUser(USER_ID, userDto);
    assertTrue(result.isPresent());
    assertEquals(updatedUser.getFirstName(), user.getFirstName());
    assertEquals(updatedUser.getLastName(), user.getLastName());
    assertNotEquals(updatedUser.getUserName(), user.getUserName());
  }

  @Test
  public void testGetUser_CannotBeFound() {
    Assert.assertTrue(userService.getUser(null).isEmpty());
    Assert.assertTrue(userService.getUser(USER_ID).isEmpty());
  }

  @Test
  public void testGetUser_Successfully() {
    User user = createUser("getUser");
    UserDto userDto = createUserDto("getUser");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(userMapper.userToUserDto(user)).thenReturn(userDto);

    Optional<UserDto> result = userService.getUser(USER_ID);
    assertTrue(result.isPresent());
    assertEquals(userDto.getUserName(), result.get().getUserName());
    assertEquals(userDto.getFirstName(), result.get().getFirstName());
    assertEquals(userDto.getLastName(), result.get().getLastName());
  }

  @Test
  public void testListUsers_CannotBeFound() {
    Assert.assertTrue(userService.listUsers().isEmpty());
  }

  @Test
  public void testListUsers_Successfully() {
    UserDto userDto = createUserDto("listUser");
    List<User> userList = new ArrayList<>() {{
      add(createUser("listUser1"));
      add(createUser("listUser2"));
    }};

    when(userRepository.findAll()).thenReturn(userList);
    when(userMapper.userToUserDto(any(User.class))).thenReturn(userDto);

    List<UserDto> result = userService.listUsers();
    assertFalse(result.isEmpty());
    assertEquals(userList.size(), result.size());
  }

  private UserDto createUserDto(String userName) {
    return UserDto.builder()
        .userName(userName)
        .firstName("dtoFirstName")
        .lastName("dtoLastName")
        .build();
  }

  private User createUser(String userName) {
    return User.builder()
        .userName(userName)
        .firstName("firstName")
        .lastName("lastName")
        .build();
  }
}
