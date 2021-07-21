package blackswan.domain.model.user;

import blackswan.domain.shared.AbstractEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * Entity representing the User data.
 *
 * @author David Molnar
 */
@Builder(builderClassName = "UserBuilder")
@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TEST_USER",
    indexes = {
        @Index(name = "TEST_USER_U1", columnList = "USER_ID", unique = true),
        @Index(name = "TEST_USER_U2", columnList = "USER_NAME", unique = true)
    })
public final class User extends AbstractEntity<User> {

  @Id
  @Column(name = "USER_ID", unique = true, nullable = false)
  @SequenceGenerator(name = "TEST_SEQ", sequenceName = "TEST_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_SEQ")
  private Long id;

  @Column(name = "USER_NAME", nullable = false, length = 240)
  private String userName;

  @Column(name = "FIRST_NAME", length = 240)
  private String firstName;

  @Column(name = "LAST_NAME", length = 240)
  private String lastName;

  private User(UserBuilder builder) {
    this.userName = builder.userName;
    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
  }

  public void merge(User other) {
    this.firstName = StringUtils.defaultIfBlank(other.firstName, this.firstName);
    this.lastName = StringUtils.defaultIfBlank(other.lastName, this.lastName);
  }

  public static class UserBuilder {

    public User build() {
      Validate.notBlank(this.userName, "userName cannot be null");

      return new User(this);
    }
  }
}
