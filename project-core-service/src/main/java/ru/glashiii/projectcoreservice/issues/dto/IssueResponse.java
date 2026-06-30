package ru.glashiii.projectcoreservice.issues.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.glashiii.projectcoreservice.issues.Issue;
import ru.glashiii.projectcoreservice.issues.IssuePriority;
import ru.glashiii.projectcoreservice.issues.IssueType;

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
    private IssuePriority priority;
    private Long assigneeId;
    private Long reporterId;
    private Instant createdAt;
    private Instant updatedAt;
    private Integer position;
    private Long columnId;
    private Long issueNumber;


    public static IssueResponse from(Issue issue) {
        return new IssueResponse(
                issue.getId(),
                issue.getProjectId(),
                issue.getType(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getPriority(),
                issue.getAssigneeId(),
                issue.getReporterId(),
                issue.getCreatedAt(),
                issue.getUpdatedAt(),
                issue.getPosition(),
                issue.getColumnId(),
                issue.getIssueNumber()

        );
    }
}
