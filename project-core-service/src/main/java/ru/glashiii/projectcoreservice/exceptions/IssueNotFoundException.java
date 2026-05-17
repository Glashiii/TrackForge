package ru.glashiii.projectcoreservice.exceptions;

public class IssueNotFoundException extends RuntimeException {
    public IssueNotFoundException(Long issueId) {
        super("Issue with id " + issueId + " not found");
    }
}
