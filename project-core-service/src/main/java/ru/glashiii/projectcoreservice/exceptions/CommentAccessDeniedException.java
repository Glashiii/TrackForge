package ru.glashiii.projectcoreservice.exceptions;

public class CommentAccessDeniedException extends RuntimeException {
    public CommentAccessDeniedException(Long commentId) {
        super("Don't have permission to comment with id: " + commentId);
    }
}
