package ru.glashiii.projectcoreservice.comments;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.glashiii.projectcoreservice.comments.dto.CommentResponse;
import ru.glashiii.projectcoreservice.comments.dto.CreateCommentRequest;
import ru.glashiii.projectcoreservice.comments.dto.UpdateCommentRequest;
import ru.glashiii.projectcoreservice.security.CurrentUserProvider;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/issues/{issueId}/comments")
public class CommentController {

    private final CommentService service;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping()
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long projectId,
            @PathVariable Long issueId,
            @RequestBody @Valid CreateCommentRequest commentRequest
    ) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        CommentResponse createdComment = service.createComment(currentUserId, projectId, issueId, commentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @GetMapping()
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long projectId,
            @PathVariable Long issueId
    ) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        List<CommentResponse> comments = service.getAllComments(currentUserId, projectId, issueId);

        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long projectId,
            @PathVariable Long issueId,
            @PathVariable Long commentId,
            @RequestBody @Valid UpdateCommentRequest commentRequest
    ) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        CommentResponse updatedComment = service.updateComment(currentUserId, projectId, issueId, commentId, commentRequest);

        return ResponseEntity.status(HttpStatus.OK).body(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long projectId,
            @PathVariable Long issueId,
            @PathVariable Long commentId
    ) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        service.deleteComment(currentUserId, projectId, issueId, commentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
