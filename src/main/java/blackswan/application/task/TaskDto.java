package blackswan.application.task;

import blackswan.infrastructure.shared.InstantSerializer;
import blackswan.infrastructure.shared.TimeStampToInstantDeSerializer;
import blackswan.domain.model.task.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.Instant;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO containing all the task information received via REST calls.
 *
 * @author David Molnar
 */
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class TaskDto {

  @JsonProperty("id")
  private Long id;

  @NotEmpty(message = "name cannot be empty")
  @JsonProperty("name")
  private String taskName;

  @JsonProperty("description")
  private String description;

  @NotNull(message = "date_time cannot be empty")
  @JsonProperty("date_time")
  @JsonDeserialize(using = TimeStampToInstantDeSerializer.class)
  @JsonSerialize(using = InstantSerializer.class)
  private Instant dateTime;

  @JsonProperty("status")
  private TaskStatus taskStatus;
}
