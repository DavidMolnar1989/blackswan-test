package blackswan.application.scheduler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Event containing a Task Id.
 *
 * @author David Molnar
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TaskCompletedEvent {

  private final Long taskId;

  /**
   * Generates a new instance of TaskCompletedEvent.
   *
   * @param taskId Long
   * @return TaskCompletedEvent
   */
  public static TaskCompletedEvent of(Long taskId) {
    return new TaskCompletedEvent(taskId);
  }
}
