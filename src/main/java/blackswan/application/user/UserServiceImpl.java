package blackswan.application.user;

import blackswan.application.mapper.UserMapper;
import blackswan.domain.model.user.User;
import blackswan.domain.model.user.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of UserService interface.
 *
 * @author David Molnar
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<UserDto> createUser(UserDto userDto) {
    User user = this.convertToEntity(userDto);

    if (user == null) {
      log.info("Incoming userDto is null");
      return Optional.empty();
    }

    Optional<User> existingUser = userRepository.findByUserName(user.getUserName());
    if (existingUser.isPresent()) {
      log.info("UserName - {} - already exists!", user.getUserName());
      return Optional.of(this.convertToDto(existingUser.get()));
    } else {
      return Optional.of(this.convertToDto(userRepository.save(user)));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Transactional
  @Override
  public Optional<UserDto> updateUser(Long userId, UserDto userDto) {
    Optional<User> userOptional = userRepository.findById(userId);

    if (userOptional.isEmpty()) {
      log.info("User doesn't exist with Id: {}", userId);
      return Optional.empty();
    }

    User incomingUser = userMapper.createUpdatedUser(userDto, userOptional.get().getUserName());

    if (incomingUser == null) {
      log.info("Incoming userDto is null");
      return Optional.empty();
    }

    userOptional.get().merge(incomingUser);

    return Optional.of(this.convertToDto(userOptional.get()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<UserDto> getUser(Long userId) {
    return userRepository.findById(userId)
        .map(this::convertToDto);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<UserDto> listUsers() {
    return userRepository.findAll()
        .stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  /**
   * This method converts the input userDto to a User entity using MapStruct mapper.
   *
   * @param userDto UserDto
   * @return the converted User entity
   */
  private User convertToEntity(UserDto userDto) {
    return userMapper.userDtoToUser(userDto);
  }

  /**
   * This method converts the input user entity to a UserDto using MapStruct mapper.
   *
   * @param user User
   * @return the converted UserDto
   */
  private UserDto convertToDto(User user) {
    return userMapper.userToUserDto(user);
  }
}
