package blackswan.domain.model.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for User entity.
 *
 * @author David Molnar
 */
public interface UserRepository extends CrudRepository<User, Long> {

  List<User> findAll();

  Optional<User> findByUserName(String userName);
}
