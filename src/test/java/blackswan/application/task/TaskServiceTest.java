package blackswan.application.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import blackswan.application.mapper.TaskMapper;
import blackswan.application.scheduler.TaskCompletedEvent;
import blackswan.domain.model.task.Task;
import blackswan.domain.model.task.TaskRepository;
import blackswan.domain.model.task.TaskStatus;
import blackswan.domain.model.user.User;
import blackswan.domain.model.user.UserRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Test class for TaskService interface.
 *
 * @author David Molnar
 */
@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {

  private static final Long USER_ID = 1L;
  private static final Long TASK_ID = 2L;
  private static final String USER_EXISTS = "userExists";

  @Mock
  private TaskRepository taskRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private TaskMapper taskMapper;
  @InjectMocks
  private TaskServiceImpl taskService;

  private Instant now;

  @Before
  public void setup() {
    now = Instant.now();
  }

  @Test
  public void testCreateTask_NonExistingUser() {
    assertTrue(taskService.createTask(TASK_ID, createTaskDto("nonExistingUser")).isEmpty());
    assertTrue(taskService.createTask(null, createTaskDto("nonExistingUser")).isEmpty());
    assertTrue(taskService.createTask(null, null).isEmpty());
  }

  @Test
  public void testCreateTask_TaskDtoIsNull() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser("taskDtoIsNull")));
    assertTrue(taskService.createTask(USER_ID, createTaskDto("nonExistingUser")).isEmpty());
    assertTrue(taskService.createTask(USER_ID, null).isEmpty());
  }

  @Test
  public void testCreateTask_Successfully() {
    TaskDto taskDto = createTaskDto("createTask");
    Task task = createTask("createTask");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    when(taskMapper.taskDtoToTask(USER_ID, taskDto)).thenReturn(task);
    when(taskRepository.save(any(Task.class))).thenAnswer((i -> i.getArguments()[0]));
    when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

    Optional<TaskDto> result = taskService.createTask(USER_ID, taskDto);
    assertTrue(result.isPresent());
    assertEquals(task.getDateTime(), result.get().getDateTime());
    assertEquals(task.getDescription(), result.get().getDescription());
    assertEquals(task.getTaskName(), result.get().getTaskName());
    assertEquals(task.getTaskStatus(), result.get().getTaskStatus());
  }

  @Test
  public void testDeleteTask_NonExistingUser() {
    assertTrue(taskService.deleteTask(USER_ID, TASK_ID).isEmpty());
    assertTrue(taskService.deleteTask(USER_ID, null).isEmpty());
    assertTrue(taskService.deleteTask(null, TASK_ID).isEmpty());
    assertTrue(taskService.deleteTask(null, null).isEmpty());
  }

  @Test
  public void testDeleteTask_CannotBeFound() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    assertTrue(taskService.deleteTask(USER_ID, TASK_ID).isEmpty());
    assertTrue(taskService.deleteTask(USER_ID, null).isEmpty());
  }

  @Test
  public void testDeleteTask_AlreadyDeleted() {
    Task task = createTask("deleteTask");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
    ReflectionTestUtils.setField(task, "activeFlag", false);

    assertTrue(taskService.deleteTask(USER_ID, TASK_ID).isEmpty());
  }

  @Test
  public void testDeleteTask_Successfully() {
    Task task = createTask("deleteTask");
    TaskDto taskDto = createTaskDto("deleteTask");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
    when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

    Optional<TaskDto> result = taskService.deleteTask(USER_ID, TASK_ID);
    assertTrue(result.isPresent());
    assertFalse(task.isActiveFlag());
    assertEquals(taskDto.getDateTime(), result.get().getDateTime());
    assertEquals(taskDto.getDescription(), result.get().getDescription());
    assertEquals(taskDto.getTaskName(), result.get().getTaskName());
  }

  @Test
  public void testUpdateTask_NonExistingUser() {
    TaskDto taskDto = createTaskDto("nonExistingUser");
    assertTrue(taskService.updateTask(USER_ID, TASK_ID, taskDto).isEmpty());
    assertTrue(taskService.updateTask(USER_ID, null, taskDto).isEmpty());
    assertTrue(taskService.updateTask(null, TASK_ID, taskDto).isEmpty());
    assertTrue(taskService.updateTask(null, null, taskDto).isEmpty());
    assertTrue(taskService.updateTask(null, null, null).isEmpty());
  }

  @Test
  public void testUpdateTask_CannotBeFound() {
    TaskDto taskDto = createTaskDto("taskNotFound");
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    assertTrue(taskService.updateTask(USER_ID, TASK_ID, taskDto).isEmpty());
    assertTrue(taskService.updateTask(USER_ID, null, null).isEmpty());
  }

  @Test
  public void testUpdateTask_FilterDeleted() {
    Task task = createTask("filterDeleted");
    ReflectionTestUtils.setField(task, "activeFlag", false);
    TaskDto taskDto = createTaskDto("filterDeleted");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

    assertTrue(taskService.updateTask(USER_ID, TASK_ID, taskDto).isEmpty());
  }

  @Test
  public void testUpdateTask_FilterDone() {
    Task task = createTask("filterDone");
    ReflectionTestUtils.setField(task, "taskStatus", TaskStatus.DONE);
    TaskDto taskDto = createTaskDto("filterDone");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

    assertTrue(taskService.updateTask(USER_ID, TASK_ID, taskDto).isEmpty());
  }

  @Test
  public void testUpdateTask_DtoIsNull() {
    Task task = createTask("dtoIsNull");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));

    assertTrue(taskService.updateTask(USER_ID, TASK_ID, null).isEmpty());
  }

  @Test
  public void testUpdateTask_Successfully_WithDateTime() {
    Task task = createTask("updateTask");
    Task incomingTask = createTask("incomingTask");
    ReflectionTestUtils.setField(task, "dateTime", Instant.now());
    ReflectionTestUtils.setField(task, "description", "incomingDesc");
    TaskDto taskDto = createTaskDto("updateTask");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
    when(taskMapper.createUpdatedTask(taskDto, task.getUserId(), taskDto.getDateTime()))
        .thenReturn(incomingTask);
    when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

    Optional<TaskDto> result = taskService.updateTask(USER_ID, TASK_ID, taskDto);
    assertTrue(result.isPresent());
    assertEquals(task.getTaskName(), incomingTask.getTaskName());
    assertEquals(task.getDateTime(), incomingTask.getDateTime());
    assertEquals(task.getDescription(), incomingTask.getDescription());
  }

  @Test
  public void testUpdateTask_Successfully_WithoutDateTime() {
    Task task = createTask("updateTask");
    Task incomingTask = createTask("incomingTask");
    TaskDto taskDto = createTaskDto("updateTask");
    ReflectionTestUtils.setField(taskDto, "dateTime", null);

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
    when(taskMapper.createUpdatedTask(taskDto, task.getUserId(), task.getDateTime()))
        .thenReturn(incomingTask);
    when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

    Optional<TaskDto> result = taskService.updateTask(USER_ID, TASK_ID, taskDto);
    assertTrue(result.isPresent());
    assertEquals(task.getTaskName(), incomingTask.getTaskName());
  }

  @Test
  public void testGetTask_NonExistingUser() {
    assertTrue(taskService.getTask(USER_ID, TASK_ID).isEmpty());
    assertTrue(taskService.getTask(USER_ID, null).isEmpty());
    assertTrue(taskService.getTask(null, TASK_ID).isEmpty());
    assertTrue(taskService.getTask(null, null).isEmpty());
  }

  @Test
  public void testGetTask_CannotBeFound() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    assertTrue(taskService.getTask(USER_ID, TASK_ID).isEmpty());
    assertTrue(taskService.getTask(USER_ID, null).isEmpty());
  }

  @Test
  public void testGetTask_Successfully() {
    Task task = createTask("getTask");
    TaskDto taskDto = createTaskDto("getTask");

    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(createUser(USER_EXISTS)));
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(task));
    when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

    Optional<TaskDto> result = taskService.getTask(USER_ID, TASK_ID);
    assertTrue(result.isPresent());
    assertEquals(taskDto.getDateTime(), result.get().getDateTime());
    assertEquals(taskDto.getDescription(), result.get().getDescription());
    assertEquals(taskDto.getTaskName(), result.get().getTaskName());
  }

  @Test
  public void testListUserTasks_CannotBeFound() {
    assertTrue(taskService.listUserTasks(USER_ID).isEmpty());
    assertTrue(taskService.listUserTasks(null).isEmpty());
  }

  @Test
  public void testListUserTasks_Successfully() {
    TaskDto taskDto = createTaskDto("listUserTask");
    List<Task> taskList = new ArrayList<>() {{
      add(createTask("listUserTask1"));
      add(createTask("listUserTask2"));
    }};

    when(taskRepository.findAllByUserId(USER_ID)).thenReturn(taskList);
    when(taskMapper.taskToTaskDto(any(Task.class))).thenReturn(taskDto);

    List<TaskDto> result = taskService.listUserTasks(USER_ID);
    assertFalse(result.isEmpty());
    assertEquals(taskList.size(), result.size());
  }

  @Test
  public void testTaskCompletedEvent_TaskNotFound() {
    TaskCompletedEvent event = TaskCompletedEvent.of(1L);
    assertTrue(taskService.handleTaskCompletedEvent(null).isEmpty());
    assertTrue(taskService.handleTaskCompletedEvent(event).isEmpty());
  }

  @Test
  public void testTaskCompletedEvent_Successful() {
    TaskCompletedEvent event = TaskCompletedEvent.of(1L);
    Task task = createTask("taskCompleted");

    when(taskRepository.findById(event.getTaskId())).thenReturn(Optional.of(task));

    Optional<Task> result = taskService.handleTaskCompletedEvent(event);
    assertTrue(result.isPresent());
    assertEquals(TaskStatus.DONE, result.get().getTaskStatus());
  }

  private TaskDto createTaskDto(String taskName) {
    return TaskDto.builder()
        .taskName(taskName)
        .description("description")
        .dateTime(now)
        .taskStatus(TaskStatus.PENDING)
        .build();
  }

  private Task createTask(String taskName) {
    return Task.builder()
        .taskName(taskName)
        .description("description")
        .dateTime(now)
        .userId(USER_ID)
        .taskStatus(TaskStatus.PENDING)
        .build();
  }

  private User createUser(String userName) {
    return User.builder()
        .userName(userName)
        .firstName("firstName")
        .lastName("lastName")
        .build();
  }
}
