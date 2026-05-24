package ru.glashiii.projectcoreservice.comments.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.glashiii.projectcoreservice.comments.Comment;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private Long issueId;
    private Long projectId;
    private Long userId;
    private String body;
    private Instant createdAt;
    private Instant updatedAt;

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getIssueId(),
                comment.getProjectId(),
                comment.getUserId(),
                comment.getBody(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
