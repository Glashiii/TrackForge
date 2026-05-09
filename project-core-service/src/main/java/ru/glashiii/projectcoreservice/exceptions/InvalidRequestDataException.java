package ru.glashiii.projectcoreservice.exceptions;

public class InvalidRequestDataException extends RuntimeException {
    public InvalidRequestDataException(String message) {
        super(message);
    }
}
