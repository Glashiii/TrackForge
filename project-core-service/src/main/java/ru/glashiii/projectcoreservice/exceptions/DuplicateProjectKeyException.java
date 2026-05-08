package ru.glashiii.projectcoreservice.exceptions;

public class DuplicateProjectKeyException extends RuntimeException {
    public DuplicateProjectKeyException(String key) {
        super("Project key already exists: " + key);
    }
}
