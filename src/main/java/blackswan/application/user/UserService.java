package blackswan.application.user;

import java.util.List;
import java.util.Optional;

/**
 * This interface defines the functionality around the User entity.
 *
 * @author David Molnar
 */
public interface UserService {

  /**
   * This method creates a new User entity.
   *
   * @param userDto UserDto from REST POST call
   * @return newly created User as a UserDto otherwise empty
   */
  Optional<UserDto> createUser(UserDto userDto);

  /**
   * This method updates an existing User entity.
   *
   * @param userId  Long - User Id
   * @param userDto UserDto from REST PUT call
   * @return the updated User as a UserDto otherwise empty
   */
  Optional<UserDto> updateUser(Long userId, UserDto userDto);

  /**
   * This method returns the User entity which is related to the input Id.
   *
   * @param userId Long - User Id
   * @return the requested User as a UserDto otherwise empty
   */
  Optional<UserDto> getUser(Long userId);

  /**
   * This method returns every User entity.
   *
   * @return every User as a UserDto in a List otherwise empty list
   */
  List<UserDto> listUsers();
}
