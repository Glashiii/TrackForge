package ru.glashiii.projectcoreservice.exceptions;

public class DuplicateEntityParamException extends RuntimeException {
    public DuplicateEntityParamException(String message) {
        super(message);
    }
}
