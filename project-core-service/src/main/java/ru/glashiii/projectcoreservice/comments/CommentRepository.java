package ru.glashiii.projectcoreservice.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.glashiii.projectcoreservice.comments.Comment;
import ru.glashiii.projectcoreservice.comments.dto.CommentResponse;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByIssueIdAndProjectIdOrderByCreatedAtAsc(Long issueId, Long projectId);
}
