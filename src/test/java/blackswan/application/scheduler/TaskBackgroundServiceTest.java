package blackswan.application.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import blackswan.domain.model.task.TaskRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Test class for TaskBackgroundService class.
 *
 * @author David Molnar
 */
@RunWith(MockitoJUnitRunner.class)
public class TaskBackgroundServiceTest {

  @Mock
  private TaskRepository taskRepository;
  @Mock
  private ApplicationEventPublisher publisher;
  @InjectMocks
  private TaskBackgroundService taskBackgroundService;

  @Test
  public void testNoPendingTaskFound() {
    assertTrue(taskBackgroundService.processPendingTasks().isEmpty());
  }

  @Test
  public void testSuccessfulExecution() {
    List<Long> taskIds = new ArrayList<>() {{
      add(1L);
      add(2L);
      add(3L);
    }};

    when(taskRepository.findAllExpiredPendingTask(any(), any(Instant.class)))
        .thenReturn(taskIds);

    List<Long> resultList = taskBackgroundService.processPendingTasks();
    assertFalse(resultList.isEmpty());
    assertEquals(taskIds.size(), resultList.size());
    verify(publisher, Mockito.times(taskIds.size())).publishEvent(any(TaskCompletedEvent.class));
  }
}
