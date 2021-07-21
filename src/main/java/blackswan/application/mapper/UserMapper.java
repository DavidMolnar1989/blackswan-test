package blackswan.application.mapper;

import blackswan.application.user.UserDto;
import blackswan.domain.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * UserMapper class using MapStruct mapper. Generates an implementation class to target/generated-sources.
 * <p>
 * The implementation class uses Lombok Builder to construct User/UserDto Objects.
 *
 * @author David Molnar
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

  /**
   * This method generates a new UserDto and maps the attributes from User entity.
   *
   * @param user User
   * @return the generated UserDto
   */
  UserDto userToUserDto(User user);

  /**
   * This method generates a new User entity and maps the attributes from UserDto. Ignores the id attribute from
   * userDto.
   *
   * @param userDto UserDto
   * @return the generated User
   */
  @Mapping(target = "id", ignore = true)
  User userDtoToUser(UserDto userDto);

  /**
   * This method generates a new User entity and maps the attributes from UserDto. Ignores the id attribute from userDto
   * and sets the mandatory userName.
   *
   * @param userDto  UserDto
   * @param userName String
   * @return the generated User
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "userName", source = "userName")
  User createUpdatedUser(UserDto userDto, String userName);
}
