package blackswan.application.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import blackswan.domain.model.task.TaskStatus;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test class for TaskController class.
 *
 * @author David Molnar
 */
@RunWith(MockitoJUnitRunner.class)
public class TaskControllerTest {

  private static final Long USER_ID = 1L;
  private static final Long TASK_ID = 2L;

  @Mock
  private TaskService taskService;

  @InjectMocks
  private TaskController taskController;

  @Test
  public void testGetTaskSuccessfully() {
    when(taskService.getTask(USER_ID, TASK_ID)).thenReturn(Optional.of(createTaskDto("getTask")));
    assertEquals(HttpStatus.OK, taskController.getTask(USER_ID, TASK_ID).getStatusCode());
  }

  @Test
  public void testGetTaskNotFound() {
    when(taskService.getTask(USER_ID, TASK_ID)).thenReturn(Optional.empty());
    assertEquals(HttpStatus.NOT_FOUND, taskController.getTask(USER_ID, TASK_ID).getStatusCode());
  }

  @Test
  public void testListUserTasksSuccessfully() {
    when(taskService.listUserTasks(USER_ID)).thenReturn(new ArrayList<>() {{
      add(createTaskDto("firstTask"));
      add(createTaskDto("secondTask"));
    }});

    ResponseEntity<List<TaskDto>> resultList = taskController.listUserTasks(USER_ID);
    assertEquals(HttpStatus.OK, resultList.getStatusCode());
    assertNotNull(resultList.getBody());
    assertEquals(2, resultList.getBody().size());
  }

  @Test
  public void testListUserTasksNotFound() {
    when(taskService.listUserTasks(USER_ID)).thenReturn(new ArrayList<>());
    assertEquals(HttpStatus.NOT_FOUND, taskController.listUserTasks(USER_ID).getStatusCode());
  }

  @Test
  public void testCreateTaskSuccessfully() {
    TaskDto taskDto = createTaskDto("createTask");
    when(taskService.createTask(USER_ID, taskDto)).thenReturn(Optional.of(taskDto));
    assertEquals(HttpStatus.OK, taskController.createTask(USER_ID, taskDto).getStatusCode());
  }

  @Test
  public void testCreateTaskNotFound() {
    TaskDto taskDto = createTaskDto("createTask");
    when(taskService.createTask(USER_ID, taskDto)).thenReturn(Optional.empty());
    assertEquals(HttpStatus.NOT_FOUND, taskController.createTask(USER_ID, taskDto).getStatusCode());
  }

  @Test
  public void testUpdateTaskSuccessfully() {
    TaskDto taskDto = createTaskDto("updateTask");
    when(taskService.updateTask(USER_ID, TASK_ID, taskDto)).thenReturn(Optional.of(taskDto));
    assertEquals(HttpStatus.OK, taskController.updateTask(USER_ID, TASK_ID, taskDto).getStatusCode());
  }

  @Test
  public void testUpdateTaskNotFound() {
    TaskDto taskDto = createTaskDto("updateTask");
    when(taskService.updateTask(USER_ID, TASK_ID, taskDto)).thenReturn(Optional.empty());
    assertEquals(HttpStatus.NOT_FOUND, taskController.updateTask(USER_ID, TASK_ID, taskDto).getStatusCode());
  }

  @Test
  public void testDeleteTaskSuccessfully() {
    TaskDto taskDto = createTaskDto("deleteTask");
    when(taskService.deleteTask(USER_ID, TASK_ID)).thenReturn(Optional.of(taskDto));
    assertEquals(HttpStatus.OK, taskController.deleteTask(USER_ID, TASK_ID).getStatusCode());
  }

  @Test
  public void testDeleteTaskNotFound() {
    TaskDto taskDto = createTaskDto("deleteTask");
    when(taskService.deleteTask(USER_ID, TASK_ID)).thenReturn(Optional.empty());
    assertEquals(HttpStatus.NOT_FOUND, taskController.deleteTask(USER_ID, TASK_ID).getStatusCode());
  }

  private TaskDto createTaskDto(String taskName) {
    return TaskDto.builder()
        .taskName(taskName)
        .description("description")
        .dateTime(Instant.now())
        .taskStatus(TaskStatus.PENDING)
        .build();
  }
}
