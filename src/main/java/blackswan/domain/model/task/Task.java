package blackswan.domain.model.task;

import blackswan.domain.shared.AbstractEntity;
import blackswan.infrastructure.shared.BooleanToStringConverter;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
 * Entity representing the Task data.
 *
 * @author David Molnar
 */
@Builder(builderClassName = "TaskBuilder")
@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TEST_TASK",
    indexes = {
        @Index(name = "TEST_USER_U1", columnList = "TASK_ID", unique = true),
        @Index(name = "TEST_USER_N1", columnList = "USER_ID")
    })
public final class Task extends AbstractEntity<Task> {

  @Id
  @Column(name = "TASK_ID", unique = true, nullable = false)
  @SequenceGenerator(name = "TEST_SEQ", sequenceName = "TEST_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TEST_SEQ")
  private Long id;

  @Column(name = "TASK_NAME", nullable = false, length = 240)
  private String taskName;

  @Column(name = "DESCRIPTION", length = 240)
  private String description;

  @Column(name = "DATE_TIME", nullable = false)
  private Instant dateTime;

  @Column(name = "TASK_STATUS", nullable = false, length = 20)
  @Enumerated(EnumType.STRING)
  private TaskStatus taskStatus;

  @Column(name = "ACTIVE_FLAG", nullable = false, length = 1)
  @Convert(converter = BooleanToStringConverter.class)
  private boolean activeFlag;

  @Column(name = "USER_ID", nullable = false)
  private Long userId;

  private Task(TaskBuilder builder) {
    this.taskName = builder.taskName;
    this.description = builder.description;
    this.dateTime = builder.dateTime;
    this.userId = builder.userId;
    this.activeFlag = builder.activeFlag;
    this.taskStatus = builder.taskStatus;
  }

  @Override
  public void merge(Task other) {
    this.taskName = StringUtils.defaultIfBlank(other.taskName, this.taskName);
    this.description = StringUtils.defaultIfBlank(other.description, this.description);
    this.dateTime = other.dateTime;
  }

  public void completeTask() {
    this.taskStatus = TaskStatus.DONE;
  }

  public void moveTaskToDeletedState() {
    this.activeFlag = false;
  }

  public static class TaskBuilder {

    public Task build() {
      Validate.notBlank(this.taskName, "taskName cannot be null");
      Validate.notNull(this.dateTime, "dateTime cannot be null");
      Validate.notNull(this.userId, "userId cannot be null");

      this.activeFlag = true;
      this.taskStatus = TaskStatus.PENDING;

      return new Task(this);
    }
  }
}
