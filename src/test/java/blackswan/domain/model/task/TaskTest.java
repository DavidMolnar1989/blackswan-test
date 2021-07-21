package blackswan.domain.model.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Task entity.
 *
 * @author David Molnar
 */
public class TaskTest {

  private static final String DESCRIPTION = "description";
  private static final String TASK_NAME = "taskName";
  private static final String DATE_TIME = "dateTime";
  private static final String USER_ID = "userId";
  private static final Long USER_ID_VALUE = 1L;
  private Task.TaskBuilder builder;
  private Instant sysdate;

  @Before
  public void setup() {
    this.builder = Task.builder();
    this.builder.id(1L);
    this.builder.description(DESCRIPTION);
    this.builder.taskStatus(TaskStatus.PENDING);
    this.builder.activeFlag(true);

    this.sysdate = Instant.now();
  }

  @Test
  public void testTaskNameValidationIssue() {
    Task task = null;

    try {
      task = this.builder.build();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains(TASK_NAME));
    }
    Assert.assertNull(task);
  }

  @Test
  public void testDateTimeValidationIssue() {
    Task task = null;
    this.builder.taskName(TASK_NAME);

    try {
      task = this.builder.build();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains(DATE_TIME));
    }
    Assert.assertNull(task);
  }

  @Test
  public void testUserIdValidationIssue() {
    Task task = null;
    this.builder.taskName(TASK_NAME);
    this.builder.dateTime(Instant.now());

    try {
      task = this.builder.build();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains(USER_ID));
    }
    Assert.assertNull(task);
  }

  @Test
  public void testCreateTask() {
    this.builder.taskName(TASK_NAME);
    this.builder.dateTime(sysdate);
    this.builder.userId(USER_ID_VALUE);
    Task task = this.builder.build();

    assertNotNull(task);
    assertEquals(TASK_NAME, task.getTaskName());
    assertEquals(DESCRIPTION, task.getDescription());
    assertEquals(USER_ID_VALUE, task.getUserId());
    assertEquals(sysdate, task.getDateTime());
    assertEquals(TaskStatus.PENDING, task.getTaskStatus());
    assertTrue(task.isActiveFlag());
  }

  @Test
  public void testCompleteTask() {
    this.builder.taskName(TASK_NAME);
    this.builder.dateTime(sysdate);
    this.builder.userId(USER_ID_VALUE);
    Task task = this.builder.build();
    task.completeTask();

    assertEquals(TaskStatus.DONE, task.getTaskStatus());
  }

  @Test
  public void testDeletedTaskState() {
    this.builder.taskName(TASK_NAME);
    this.builder.dateTime(sysdate);
    this.builder.userId(USER_ID_VALUE);
    Task task = this.builder.build();
    task.moveTaskToDeletedState();

    assertFalse(task.isActiveFlag());
  }

  @Test
  public void testMergeSuccessful() {
    this.builder.taskName(TASK_NAME);
    this.builder.dateTime(sysdate);
    this.builder.userId(USER_ID_VALUE);
    Task task = this.builder.build();

    Task otherTask = Task.builder()
        .taskName("one")
        .description("two")
        .userId(2L)
        .dateTime(Instant.now())
        .build();

    task.merge(otherTask);

    assertEquals(task.getTaskName(), otherTask.getTaskName());
    assertEquals(task.getDescription(), otherTask.getDescription());
    assertEquals(task.getDateTime(), otherTask.getDateTime());
    Assert.assertNull(task.getId());
  }

  @Test
  public void testMergeDefaultValues() {
    this.builder.taskName(TASK_NAME);
    this.builder.dateTime(sysdate);
    this.builder.userId(USER_ID_VALUE);
    Task task = this.builder.build();

    Task otherTask = Task.builder()
        .taskName(TASK_NAME)
        .description(null)
        .userId(2L)
        .dateTime(Instant.now())
        .build();

    task.merge(otherTask);

    assertEquals(TASK_NAME, task.getTaskName());
    assertEquals(DESCRIPTION, task.getDescription());
  }
}
