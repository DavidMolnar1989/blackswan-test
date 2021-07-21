package blackswan.application.scheduler;

import blackswan.domain.model.task.TaskRepository;
import blackswan.domain.model.task.TaskStatus;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Background service for handling pending Tasks.
 *
 * @author David Molnar
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskBackgroundService {

  private final TaskRepository taskRepository;
  private final ApplicationEventPublisher publisher;

  /**
   * Reads every Pending, active (non deleted) Task from the database which are expired.
   *
   * @return List of Task Id's which were moved to DONE state.
   */
  @Scheduled(fixedDelayString = "${background.thread.frequency}",
      initialDelayString = "${background.thread.frequency}")
  public List<Long> processPendingTasks() {
    log.info("Scheduled job for processing PENDING Tasks started");

    List<Long> taskIds = taskRepository.findAllExpiredPendingTask(TaskStatus.PENDING, Instant.now());

    taskIds.forEach(
        taskId -> {
          log.info("Expired task[{}], move to Done state", taskId);
          publisher.publishEvent(TaskCompletedEvent.of(taskId));
        }
    );

    return taskIds;
  }
}
