package ru.glashiii.projectcoreservice.comments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.glashiii.projectcoreservice.comments.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
