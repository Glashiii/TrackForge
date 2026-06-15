package ru.glashiii.projectcoreservice.issues.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.glashiii.projectcoreservice.issues.IssuePriority;
import ru.glashiii.projectcoreservice.issues.IssueType;

@Data
public class IssueCreateRequest {

    @NotNull
    private IssueType issueType;

    @NotBlank
    @Size(min = 1, max = 64)
    private String title;

    @Size(max = 255)
    private String description;

    @NotNull
    private IssuePriority priority;

    private Long assigneeId;
}
