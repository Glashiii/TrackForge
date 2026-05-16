package ru.glashiii.projectcoreservice.exceptions;

public class IssueAccessDeniedException extends RuntimeException {
    public IssueAccessDeniedException(Long userId, Long projectId) {
        super("User " + userId + " has no access to create issues in project " + projectId);
    }
}
