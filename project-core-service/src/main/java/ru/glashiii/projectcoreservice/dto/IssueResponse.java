package ru.glashiii.projectcoreservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.glashiii.projectcoreservice.entities.Issue;
import ru.glashiii.projectcoreservice.entities.IssuePriority;
import ru.glashiii.projectcoreservice.entities.IssueStatus;
import ru.glashiii.projectcoreservice.entities.IssueType;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class IssueResponse {
    private Long id;
    private Long projectId;
    private IssueType type;
    private String title;
    private String description;
    private IssueStatus status;
    private IssuePriority priority;
    private Long assigneeId;
    private Long reporterId;
    private Instant createdAt;
    private Instant updatedAt;


    public static IssueResponse from(Issue issue) {
        return new IssueResponse(
                issue.getId(),
                issue.getProjectId(),
                issue.getType(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getStatus(),
                issue.getPriority(),
                issue.getAssigneeId(),
                issue.getReporterId(),
                issue.getCreatedAt(),
                issue.getUpdatedAt()
        );
    }
}
