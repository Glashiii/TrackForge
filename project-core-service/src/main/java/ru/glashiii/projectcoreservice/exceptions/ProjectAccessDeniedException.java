package ru.glashiii.projectcoreservice.exceptions;

public class ProjectAccessDeniedException extends RuntimeException {
    public ProjectAccessDeniedException(Long projectId) {
        super("Don't have permission to access project with id " + projectId);
    }
}
