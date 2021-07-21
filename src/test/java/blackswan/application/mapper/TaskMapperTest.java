package blackswan.application.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import blackswan.application.task.TaskDto;
import blackswan.domain.model.task.Task;
import blackswan.domain.model.task.TaskStatus;
import java.time.Instant;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for TaskMapper interface.
 *
 * @author David Molnar
 */
public class TaskMapperTest {

  private static final Long USER_ID = 1L;
  private TaskMapper taskMapper;

  @Before
  public void setup() {
    taskMapper = new TaskMapperImpl();
  }

  @Test
  public void testTaskToTaskDto_NullInput() {
    assertNull(taskMapper.taskToTaskDto(null));
  }

  @Test
  public void testTaskToTaskDto_Successful() {
    Task task = createTask();
    TaskDto taskDto = taskMapper.taskToTaskDto(task);

    assertEquals(task.getTaskName(), taskDto.getTaskName());
    assertEquals(task.getDescription(), taskDto.getDescription());
    assertEquals(task.getTaskStatus(), taskDto.getTaskStatus());
    assertNull(taskDto.getId());
  }

  @Test
  public void testTaskDtoToTask_NullInputs() {
    assertNull(taskMapper.taskDtoToTask(null, null));
  }

  @Test
  public void testTaskDtoToTask_NullTaskDtoInput() {
    Task task = null;

    try {
      task = taskMapper.taskDtoToTask(USER_ID, null);
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("taskName"));
    }

    assertNull(task);
  }

  @Test
  public void testTaskDtoToTask_NullUserIdInput() {
    TaskDto taskDto = createTaskDto();
    Task task = null;

    try {
      task = taskMapper.taskDtoToTask(null, taskDto);
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("userId"));
    }

    assertNull(task);
  }

  @Test
  public void testTaskDtoToTask_Successful() {
    TaskDto taskDto = createTaskDto();
    Task task = taskMapper.taskDtoToTask(USER_ID, taskDto);

    assertEquals(taskDto.getTaskName(), task.getTaskName());
    assertEquals(taskDto.getDescription(), task.getDescription());
    assertEquals(taskDto.getTaskStatus(), task.getTaskStatus());
    assertNull(task.getId());
  }

  @Test
  public void testCreateUpdatedTask_NullInputs() {
    assertNull(taskMapper.createUpdatedTask(null, null, null));
  }

  @Test
  public void testCreateUpdatedTask_NullTaskDtoInput() {
    Task task = null;

    try {
      task = taskMapper.createUpdatedTask(null, USER_ID, Instant.now());
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("taskName"));
    }

    assertNull(task);
  }

  @Test
  public void testCreateUpdatedTask_NullUserIdInput() {
    TaskDto taskDto = createTaskDto();
    Task task = null;

    try {
      task = taskMapper.createUpdatedTask(taskDto, null, Instant.now());
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("userId"));
    }

    assertNull(task);
  }

  @Test
  public void testCreateUpdatedTask_NullDateTimeInput() {
    TaskDto taskDto = createTaskDto();
    Task task = null;

    try {
      task = taskMapper.createUpdatedTask(taskDto, USER_ID, null);
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("dateTime"));
    }

    assertNull(task);
  }

  @Test
  public void testCreateUpdatedTask_Successful() {
    TaskDto taskDto = createTaskDto();
    Instant now = Instant.now();
    Task task = taskMapper.createUpdatedTask(taskDto, USER_ID, now);

    assertEquals(taskDto.getTaskName(), task.getTaskName());
    assertEquals(taskDto.getDescription(), task.getDescription());
    assertEquals(taskDto.getTaskStatus(), task.getTaskStatus());
    assertEquals(now, task.getDateTime());
    assertEquals(USER_ID, task.getUserId());
    assertNull(task.getId());
  }

  private TaskDto createTaskDto() {
    return TaskDto.builder()
        .taskName("dtoTaskName")
        .description("dtoDescription")
        .dateTime(Instant.now())
        .taskStatus(TaskStatus.PENDING)
        .build();
  }

  private Task createTask() {
    return Task.builder()
        .taskName("taskName")
        .description("description")
        .dateTime(Instant.now())
        .userId(USER_ID)
        .taskStatus(TaskStatus.PENDING)
        .build();
  }
}
