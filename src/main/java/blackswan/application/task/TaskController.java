package blackswan.application.task;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController class handling REST calls related to the Task entity.
 *
 * @author David Molnar
 */
@RestController
@RequestMapping("/user/{user_id}")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskController {

  private final TaskService taskService;

  @PostMapping(path = "/task")
  public ResponseEntity<TaskDto> createTask(@PathVariable("user_id") Long userId,
      @Valid @RequestBody TaskDto taskDto) {
    return ResponseEntity.of(taskService.createTask(userId, taskDto));
  }

  @GetMapping(path = "/task/{task_id}")
  public ResponseEntity<TaskDto> getTask(@PathVariable("user_id") Long userId, @PathVariable("task_id") Long taskId) {
    return ResponseEntity.of(taskService.getTask(userId, taskId));
  }

  @PutMapping(path = "/task/{task_id}")
  public ResponseEntity<TaskDto> updateTask(@PathVariable("user_id") Long userId, @PathVariable("task_id") Long taskId,
      @RequestBody TaskDto taskDto) {
    return ResponseEntity.of(taskService.updateTask(userId, taskId, taskDto));
  }

  @GetMapping(path = "/task")
  public ResponseEntity<List<TaskDto>> listUserTasks(@PathVariable("user_id") Long userId) {
    List<TaskDto> tasks = taskService.listUserTasks(userId);
    return !tasks.isEmpty() ? ResponseEntity.ok(tasks) : ResponseEntity.notFound().build();
  }

  @DeleteMapping(path = "/task/{task_id}")
  public ResponseEntity<TaskDto> deleteTask(@PathVariable("user_id") Long userId,
      @PathVariable("task_id") Long taskId) {
    return ResponseEntity.of(taskService.deleteTask(userId, taskId));
  }
}
