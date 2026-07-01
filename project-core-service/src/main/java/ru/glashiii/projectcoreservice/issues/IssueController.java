package ru.glashiii.projectcoreservice.issues;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.glashiii.projectcoreservice.issues.dto.IssueCreateRequest;
import ru.glashiii.projectcoreservice.issues.dto.IssueMoveRequest;
import ru.glashiii.projectcoreservice.issues.dto.IssueResponse;
import ru.glashiii.projectcoreservice.issues.dto.IssueUpdateRequest;
import ru.glashiii.projectcoreservice.security.CurrentUserProvider;
import ru.glashiii.projectcoreservice.issues.IssueService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("projects/{projectId}/issues")
public class IssueController {

    private final IssueService issueService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping
    public ResponseEntity<IssueResponse> createIssue(
            @PathVariable Long projectId,
            @Valid @RequestBody IssueCreateRequest request
    ) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        IssueResponse createdIssue = issueService.createIssue(currentUserId, projectId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdIssue);
    }

    @GetMapping
    public ResponseEntity<List<IssueResponse>> getIssues(
            @PathVariable Long projectId
    ) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        List<IssueResponse> issues = issueService.getAllIssuesInProject(currentUserId, projectId);

        return ResponseEntity.ok(issues);
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<IssueResponse> getIssue(
            @PathVariable Long projectId,
            @PathVariable Long issueId
    ) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        IssueResponse issue = issueService.getIssue(currentUserId, projectId, issueId);

        return ResponseEntity.ok(issue);
    }

    @PatchMapping("/{issueId}")
    public ResponseEntity<IssueResponse> updateIssue(
            @PathVariable Long projectId,
            @PathVariable Long issueId,
            @Valid @RequestBody IssueUpdateRequest request
    ) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        IssueResponse issueResponse = issueService.updateIssue(currentUserId, projectId, issueId, request);

        return ResponseEntity.ok(issueResponse);
    }

    @PatchMapping("/{issueId}/move")
    public ResponseEntity<IssueResponse> moveIssue(
            @PathVariable Long projectId,
            @PathVariable Long issueId,
            @Valid @RequestBody IssueMoveRequest request
    ) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        IssueResponse issueResponse = issueService.moveIssue(currentUserId, projectId, issueId, request);

        return ResponseEntity.ok(issueResponse);
    }


    @DeleteMapping("/{issueId}")
    public ResponseEntity<Void> deleteIssue(
            @PathVariable Long projectId,
            @PathVariable Long issueId

    ) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        issueService.deleteIssue(currentUserId, projectId, issueId);
        return ResponseEntity.noContent().build();
    }

}
