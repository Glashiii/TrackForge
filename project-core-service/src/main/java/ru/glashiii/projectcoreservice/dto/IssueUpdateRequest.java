package ru.glashiii.projectcoreservice.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.glashiii.projectcoreservice.entities.IssuePriority;
import ru.glashiii.projectcoreservice.entities.IssueStatus;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueUpdateRequest {

    @Size(min = 1, max = 64)
    private String title;
    @Size(min = 1, max = 255)
    private String description;
    private IssueStatus status;
    private IssuePriority priority;
    private Long assigneeId;

}
