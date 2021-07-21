package blackswan.application.user;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController class handling REST calls related to the User entity.
 *
 * @author David Molnar
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

  private final UserService userService;

  @PostMapping(path = "/user")
  public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
    return ResponseEntity.of(userService.createUser(userDto));
  }

  @GetMapping(path = "/user/{id}")
  public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
    return ResponseEntity.of(userService.getUser(id));
  }

  @PutMapping(path = "/user/{id}")
  public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
    return ResponseEntity.of(userService.updateUser(id, userDto));
  }

  @GetMapping(path = "/user")
  public ResponseEntity<List<UserDto>> listUsers() {
    List<UserDto> users = userService.listUsers();
    return !users.isEmpty() ? ResponseEntity.ok(users) : ResponseEntity.notFound().build();
  }
}
