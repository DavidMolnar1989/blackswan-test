package blackswan.application.task;

import blackswan.application.mapper.TaskMapper;
import blackswan.application.scheduler.TaskCompletedEvent;
import blackswan.domain.model.task.Task;
import blackswan.domain.model.task.TaskRepository;
import blackswan.domain.model.task.TaskStatus;
import blackswan.domain.model.user.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of TaskService interface.
 *
 * @author David Molnar
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;
  private final UserRepository userRepository;
  private final TaskMapper taskMapper;

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<TaskDto> createTask(Long userId, TaskDto taskDto) {
    if (isNonExistingUser(userId)) {
      return Optional.empty();
    }

    Task task = this.convertToEntity(userId, taskDto);
    if (task == null) {
      log.info("Incoming taskDto is null");
      return Optional.empty();
    }

    return Optional.of(this.convertToDto(taskRepository.save(task)));
  }

  /**
   * {@inheritDoc}
   */
  @Transactional
  @Override
  public Optional<TaskDto> deleteTask(Long userId, Long taskId) {
    if (isNonExistingUser(userId)) {
      return Optional.empty();
    }

    Optional<Task> taskOptional = taskRepository.findById(taskId);
    if (taskOptional.isEmpty()) {
      log.info("Task doesn't exist with Id: {}", taskId);
      return Optional.empty();
    }

    if (!taskOptional.get().isActiveFlag()) {
      log.info("Task[{}] is already deleted!", taskId);
      return Optional.empty();
    }

    taskOptional.get().moveTaskToDeletedState();

    return Optional.of(this.convertToDto(taskOptional.get()));
  }

  /**
   * {@inheritDoc}
   */
  @Transactional
  @Override
  public Optional<TaskDto> updateTask(Long userId, Long taskId, TaskDto taskDto) {
    if (isNonExistingUser(userId)) {
      return Optional.empty();
    }

    Optional<Task> taskOptional = taskRepository
        .findById(taskId)
        .filter(Task::isActiveFlag)
        .filter(task -> TaskStatus.PENDING.equals(task.getTaskStatus()));
    if (taskOptional.isEmpty()) {
      log.info("Task cannot be updated with Id: {}", taskId);
      return Optional.empty();
    }

    Task incomingTask = taskMapper.createUpdatedTask(
        taskDto,
        taskOptional.get().getUserId(),
        taskDto != null && taskDto.getDateTime() != null ? taskDto.getDateTime() : taskOptional.get().getDateTime()
    );
    if (incomingTask == null) {
      log.info("Incoming taskDto is null");
      return Optional.empty();
    }

    taskOptional.get().merge(incomingTask);

    return Optional.of(this.convertToDto(taskOptional.get()));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<TaskDto> getTask(Long userId, Long taskId) {
    if (isNonExistingUser(userId)) {
      return Optional.empty();
    }

    return taskRepository.findById(taskId)
        .filter(Task::isActiveFlag)
        .map(this::convertToDto);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TaskDto> listUserTasks(Long userId) {
    return taskRepository.findAllByUserId(userId)
        .stream()
        .filter(Task::isActiveFlag)
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Transactional
  @EventListener
  @Override
  public Optional<Task> handleTaskCompletedEvent(TaskCompletedEvent event) {
    if (event == null) {
      return Optional.empty();
    }

    Optional<Task> taskOptional = taskRepository.findById(event.getTaskId());
    taskOptional.ifPresent(
        Task::completeTask
    );

    return taskOptional;
  }

  /**
   * This method converts the input taskDto to a Task entity using MapStruct mapper.
   *
   * @param userId  Long - User Id
   * @param taskDto TaskDto
   * @return the converted Task entity
   */
  private Task convertToEntity(Long userId, TaskDto taskDto) {
    return taskMapper.taskDtoToTask(userId, taskDto);
  }

  /**
   * This method converts the input task entity to a TaskDto using MapStruct mapper.
   *
   * @param task Task
   * @return the converted TaskDto
   */
  private TaskDto convertToDto(Task task) {
    return taskMapper.taskToTaskDto(task);
  }

  /**
   * This method checks whether the User exists in the Database or not.
   *
   * @param userId Long
   * @return User exists or not
   */
  private boolean isNonExistingUser(Long userId) {
    if (userRepository.findById(userId).isEmpty()) {
      log.info("User with Id: {} doesn't exist!", userId);
      return true;
    }
    return false;
  }
}
