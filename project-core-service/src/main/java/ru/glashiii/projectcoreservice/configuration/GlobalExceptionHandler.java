package ru.glashiii.projectcoreservice.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.glashiii.projectcoreservice.exceptions.DuplicateProjectKeyException;
import ru.glashiii.projectcoreservice.exceptions.UnauthorizedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(DuplicateProjectKeyException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateProjectKey(DuplicateProjectKeyException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
        problem.setTitle("Project key already exists");
        problem.setProperty("code", "PROJECT_KEY_ALREADY_EXISTS");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }
}
