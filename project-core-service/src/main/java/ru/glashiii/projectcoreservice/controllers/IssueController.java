package ru.glashiii.projectcoreservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.glashiii.projectcoreservice.dto.IssueCreateRequest;
import ru.glashiii.projectcoreservice.dto.IssueResponse;
import ru.glashiii.projectcoreservice.entities.Issue;
import ru.glashiii.projectcoreservice.security.CurrentUserProvider;
import ru.glashiii.projectcoreservice.services.IssueService;

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


}
