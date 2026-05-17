package ru.glashiii.projectcoreservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.glashiii.projectcoreservice.dto.IssueCreateRequest;
import ru.glashiii.projectcoreservice.dto.IssueResponse;
import ru.glashiii.projectcoreservice.security.CurrentUserProvider;
import ru.glashiii.projectcoreservice.services.IssueService;

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
