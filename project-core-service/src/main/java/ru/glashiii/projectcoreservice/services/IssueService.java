package ru.glashiii.projectcoreservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.glashiii.projectcoreservice.dto.IssueCreateRequest;
import ru.glashiii.projectcoreservice.dto.IssueResponse;
import ru.glashiii.projectcoreservice.entities.*;
import ru.glashiii.projectcoreservice.exceptions.DuplicateEntityParamException;
import ru.glashiii.projectcoreservice.exceptions.InvalidRequestDataException;
import ru.glashiii.projectcoreservice.exceptions.IssueAccessDeniedException;
import ru.glashiii.projectcoreservice.exceptions.ProjectNotFoundException;
import ru.glashiii.projectcoreservice.repositories.IssueRepository;
import ru.glashiii.projectcoreservice.repositories.ProjectMemberRepository;
import ru.glashiii.projectcoreservice.repositories.ProjectRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public IssueResponse createIssue(Long userId, Long projectId, IssueCreateRequest issueCreateRequest) {
        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        Long issueNumber = project.getNextIssueNumber();

        project.setNextIssueNumber(issueNumber + 1L);

        if (member.getRole() == ProjectRole.VIEWER){
            throw new IssueAccessDeniedException(userId, projectId);
        }

/*        if (issueRepository.existsByProjectIdAndTitle(projectId, issueCreateRequest.getTitle())) {
            throw new DuplicateEntityParamException("Issue with this name already exists in this project " + projectId);
        }*/


        if (issueCreateRequest.getAssigneeId() != null
            && !projectMemberRepository.existsByProjectIdAndUserId(projectId, issueCreateRequest.getAssigneeId())){
            throw new InvalidRequestDataException("Assignee must be a project member");
        }

        Issue issue = Issue.builder()
                .projectId(projectId)
                .type(issueCreateRequest.getIssueType())
                .title(issueCreateRequest.getTitle())
                .description(issueCreateRequest.getDescription())
                .status(IssueStatus.TODO)
                .priority(issueCreateRequest.getPriority())
                .assigneeId(issueCreateRequest.getAssigneeId())
                .reporterId(userId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .issueNumber(issueNumber)
                .build();

        try {
            Issue saved =  issueRepository.saveAndFlush(issue);
            return IssueResponse.from(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityParamException("Issue with this name already exists in this project " + projectId);
        }

    }
}
