package ru.glashiii.projectcoreservice.exceptions;

public class MemberAlreadyExists extends RuntimeException {
    public MemberAlreadyExists(Long projectId, Long userId) {
        super("User with id: " + userId + " already member of project: " + projectId);
    }
}
