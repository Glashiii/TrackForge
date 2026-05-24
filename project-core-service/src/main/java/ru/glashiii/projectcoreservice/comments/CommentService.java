package ru.glashiii.projectcoreservice.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.glashiii.projectcoreservice.comments.dto.CommentResponse;
import ru.glashiii.projectcoreservice.comments.dto.CreateCommentRequest;
import ru.glashiii.projectcoreservice.comments.dto.UpdateCommentRequest;
import ru.glashiii.projectcoreservice.exceptions.*;
import ru.glashiii.projectcoreservice.issues.IssueRepository;
import ru.glashiii.projectcoreservice.projects.ProjectMember;
import ru.glashiii.projectcoreservice.projects.ProjectMemberRepository;
import ru.glashiii.projectcoreservice.projects.ProjectRole;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final IssueRepository issueRepository;


    @Transactional
    public CommentResponse createComment(Long userId, Long projectId, Long issueId, CreateCommentRequest commentRequest) {
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (projectMember.getRole() == ProjectRole.VIEWER) {
            throw new ProjectAccessDeniedException(projectId);
        }

        issueRepository.findByIdAndProjectId(issueId, projectId).orElseThrow(() -> new IssueNotFoundException(issueId));

        Instant now = Instant.now();

        Comment commentToCreate = Comment.builder()
                .issueId(issueId)
                .projectId(projectId)
                .userId(userId)
                .body(commentRequest.getBody().trim())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Comment savedComment;

        try {
            savedComment = commentRepository.saveAndFlush(commentToCreate);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityParamException("Comment already exists");
        }

        return CommentResponse.from(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getAllComments(Long userId, Long projectId, Long issueId) {
        projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        issueRepository.findByIdAndProjectId(issueId, projectId).orElseThrow(() -> new IssueNotFoundException(issueId));

        List<Comment> commentResponseList = commentRepository.findAllByIssueIdAndProjectIdOrderByCreatedAtAsc(issueId, projectId);

        return commentResponseList
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public CommentResponse updateComment(Long userId, Long projectId, Long issueId, Long commentId, UpdateCommentRequest commentRequest) {
        projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        issueRepository.findByIdAndProjectId(issueId, projectId)
                .orElseThrow(() -> new IssueNotFoundException(issueId));

        Comment comment = commentRepository.findByIdAndIssueIdAndProjectId(commentId, issueId, projectId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!Objects.equals(comment.getUserId(), userId)) {
            throw new CommentAccessDeniedException(commentId);
        }

        comment.setBody(commentRequest.getBody().trim());
        comment.setUpdatedAt(Instant.now());

        Comment updatedComment = commentRepository.saveAndFlush(comment);

        return  CommentResponse.from(updatedComment);
    }
}
