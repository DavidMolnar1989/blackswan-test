package blackswan.application.task;

import blackswan.application.scheduler.TaskCompletedEvent;
import blackswan.domain.model.task.Task;
import java.util.List;
import java.util.Optional;

/**
 * This interface defines the functionality around the Task entity.
 *
 * @author David Molnar
 */
public interface TaskService {

  /**
   * This method creates a new Task entity and connects it to the User entity.
   *
   * @param taskDto TaskDto from REST POST call
   * @param userId  Long - the Task will be connected to the User related to the userId
   * @return newly created Task as a TaskDto otherwise empty
   */
  Optional<TaskDto> createTask(Long userId, TaskDto taskDto);

  /**
   * This method deletes an existing Task entity, what was not deleted before. During the deletion the activeFlag of the
   * Task will be set to false. No physical deletion will happen.
   *
   * @param userId Long - the Task is related to the userId
   * @param taskId Long - the Task related to the taskId will be deleted
   * @return the deleted Task as a TaskDto otherwise empty
   */
  Optional<TaskDto> deleteTask(Long userId, Long taskId);

  /**
   * This method updates an existing Task entity.
   *
   * @param userId  Long - the Task is related to the userId
   * @param taskId  Long - the Task related to the taskId will be updated
   * @param taskDto TaskDto from REST PUT call
   * @return the updated Task as a TaskDto otherwise empty
   */
  Optional<TaskDto> updateTask(Long userId, Long taskId, TaskDto taskDto);

  /**
   * This method returns the Task entity which is related to the input userId and taskId.
   *
   * @param userId Long - User Id
   * @param taskId Long- Task Id
   * @return the requested Task as a TaskDto otherwise empty
   */
  Optional<TaskDto> getTask(Long userId, Long taskId);

  /**
   * This method returns every Task entity related to the input userId.
   *
   * @param userId Long - User Id
   * @return every Task as a TaskDto in a List otherwise empty list
   */
  List<TaskDto> listUserTasks(Long userId);

  /**
   * The task related to the received event will be moved to Done state.
   *
   * @param event TaskCompletedEvent
   * @return Actual Task containing Done taskStatus.
   */
  Optional<Task> handleTaskCompletedEvent(TaskCompletedEvent event);
}
