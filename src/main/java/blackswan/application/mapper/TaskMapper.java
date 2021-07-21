package blackswan.application.mapper;

import blackswan.application.task.TaskDto;
import blackswan.domain.model.task.Task;
import java.time.Instant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * TaskMapper class using MapStruct mapper. Generates an implementation class to target/generated-sources.
 * <p>
 * The implementation class uses Lombok Builder to construct Task/TaskDto Objects.
 *
 * @author David Molnar
 */
@Mapper(componentModel = "spring")
public interface TaskMapper {

  /**
   * This method generates a new TaskDto and maps the attributes from Task entity.
   *
   * @param task Task
   * @return the generated TaskDto
   */
  TaskDto taskToTaskDto(Task task);

  /**
   * This method generates a new Task entity and maps the attributes from TaskDto. Ignores the id attribute from taskDto
   * and sets the mandatory userId.
   *
   * @param taskDto TaskDto
   * @param userId  Long - User Id, which is stored on Task entity
   * @return the generated Task
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(source = "userId", target = "userId")
  Task taskDtoToTask(Long userId, TaskDto taskDto);

  /**
   * This method generates a new Task entity and maps the attributes from TaskDto. Ignores the id attribute from taskDto
   * and sets the mandatory userId, dateTime.
   *
   * @param taskDto  TaskDto
   * @param userId   Long - userId
   * @param dateTime Instant
   * @return the generated Task
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "userId", source = "userId")
  @Mapping(target = "dateTime", source = "dateTime")
  Task createUpdatedTask(TaskDto taskDto, Long userId, Instant dateTime);
}
