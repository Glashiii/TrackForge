package ru.glashiii.projectcoreservice.configuration;

import jakarta.persistence.ElementCollection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.glashiii.projectcoreservice.exceptions.*;

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

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleProjectNotFound(ProjectNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problem.setTitle("Project not found");
        problem.setProperty("code", "PROJECT_NOT_FOUND");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(ProjectAccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleProjectAccessDenied(ProjectAccessDeniedException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                ex.getMessage()
        );
        problem.setTitle("Project access denied");
        problem.setProperty("code", "PROJECT_ACCESS_DENIED");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problem);
    }

    @ExceptionHandler(InvalidRequestDataException.class)
    public ResponseEntity<ProblemDetail> handleInvalidRequestData(InvalidRequestDataException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        problem.setTitle("Invalid request data");
        problem.setProperty("code", "INVALID_REQUEST");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(MemberAlreadyExists.class)
    public ResponseEntity<ProblemDetail> handleMemberAlreadyExists(MemberAlreadyExists ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );

        problem.setTitle("Member already exists");
        problem.setProperty("code", "MEMBER_ALREADY_EXISTS");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(DuplicateEntityParamException.class)
    public ResponseEntity<ProblemDetail> handleDuplicate(DuplicateEntityParamException ex){
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
        problem.setTitle("Duplicate entity");
        problem.setProperty("code", "DUPLICATE_ENTITY");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }

    @ExceptionHandler(IssueAccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleIssueAccessDenied(IssueAccessDeniedException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                ex.getMessage()
        );
        problem.setTitle("Issue access denied");
        problem.setProperty("code", "ISSUE_ACCESS_DENIED");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problem);
    }

    @ExceptionHandler(IssueNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleIssueNotFound(IssueNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problem.setTitle("Issue not found");
        problem.setProperty("code", "ISSUE_NOT_FOUND");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(CommentAccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleCommentAccessDenied(CommentAccessDeniedException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                ex.getMessage()
        );
        problem.setTitle("Comment access denied");
        problem.setProperty("code", "COMMENT_ACCESS_DENIED");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problem);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCommentNotFound(CommentNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problem.setTitle("Comment not found");
        problem.setProperty("code", "COMMENT_NOT_FOUND");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }
}
