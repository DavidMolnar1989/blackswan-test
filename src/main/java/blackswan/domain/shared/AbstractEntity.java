package blackswan.domain.shared;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Abstract class representing default behavior. Entity classes must extend this Abstract class.
 *
 * @param <T>
 * @author David Molnar
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class AbstractEntity<T> {

  @Column(name = "CREATED_DATE", nullable = false)
  @CreatedDate
  private Instant createdDate;

  @Column(name = "LAST_MODIFIED_DATE", nullable = false)
  @LastModifiedDate
  @Getter(AccessLevel.PUBLIC)
  private Instant lastModifiedDate;

  @Column(name = "VERSION")
  @Version
  private long version;

  public abstract void merge(T other);
}
