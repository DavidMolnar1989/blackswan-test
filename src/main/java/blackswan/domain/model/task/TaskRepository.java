package blackswan.domain.model.task;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Repository for Task entity.
 *
 * @author David Molnar
 */
public interface TaskRepository extends CrudRepository<Task, Long> {

  List<Task> findAllByUserId(Long userId);

  @Query("select task.id from Task task where task.taskStatus = :taskStatus and "
      + "task.dateTime < :paramDate and task.activeFlag = true")
  List<Long> findAllExpiredPendingTask(@Param("taskStatus") TaskStatus taskStatus,
      @Param("paramDate") Instant paramDate);
}
