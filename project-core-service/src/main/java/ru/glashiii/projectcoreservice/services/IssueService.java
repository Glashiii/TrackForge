package ru.glashiii.projectcoreservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.glashiii.projectcoreservice.dto.IssueCreateRequest;
import ru.glashiii.projectcoreservice.dto.IssueResponse;
import ru.glashiii.projectcoreservice.dto.IssueUpdateRequest;
import ru.glashiii.projectcoreservice.entities.*;
import ru.glashiii.projectcoreservice.exceptions.*;
import ru.glashiii.projectcoreservice.repositories.IssueRepository;
import ru.glashiii.projectcoreservice.repositories.ProjectMemberRepository;
import ru.glashiii.projectcoreservice.repositories.ProjectRepository;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

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

    @Transactional(readOnly = true)
    public List<IssueResponse> getAllIssuesInProject(Long userId, Long projectId) {
        projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        List<Issue> issues = issueRepository.findAllByProjectIdOrderByIssueNumberAsc(projectId);

        return issues.stream().map(IssueResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public IssueResponse getIssue(Long userId, Long projectId, Long issueId) {
        projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        Issue issue = issueRepository.findByIdAndProjectId(issueId, projectId).orElseThrow(() -> new IssueNotFoundException(issueId));

        return IssueResponse.from(issue);
    }

    @Transactional
    public void deleteIssue(Long userId, Long projectId, Long issueId) {
        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        Issue issue = issueRepository.findByIdAndProjectId(issueId, projectId).orElseThrow(
                () -> new IssueNotFoundException(issueId));

        if (member.getRole() == ProjectRole.VIEWER){
            throw new IssueAccessDeniedException(userId, projectId);
        }

        if (!Objects.equals(issue.getReporterId(), userId) && member.getRole() != ProjectRole.OWNER){
            throw new IssueAccessDeniedException(userId, projectId);
        }

        issueRepository.delete(issue);
    }

    @Transactional
    public IssueResponse updateIssue(Long userId, Long projectId, Long issueId, IssueUpdateRequest request) {
        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        Issue issue = issueRepository.findByIdAndProjectId(issueId, projectId).orElseThrow(
                () -> new IssueNotFoundException(issueId));

        if (member.getRole() == ProjectRole.VIEWER){
            throw new IssueAccessDeniedException(userId, projectId);
        }

        if (!Objects.equals(issue.getReporterId(), userId) && member.getRole() != ProjectRole.OWNER){
            throw new IssueAccessDeniedException(userId, projectId);
        }

        if (request.getTitle() != null) {
            if (request.getTitle().isBlank()) {
                throw new InvalidRequestDataException("Title cannot be blank");
            }
            issue.setTitle(request.getTitle().trim());
        }

        if (request.getDescription() != null) {
            issue.setDescription(request.getDescription().trim());
        }

        if (request.getStatus() != null) {
            issue.setStatus(request.getStatus());
        }

        if (request.getPriority() != null) {
            issue.setPriority(request.getPriority());
        }

        if (request.getAssigneeId() != null) {
            projectMemberRepository.findByProjectIdAndUserId(projectId, request.getAssigneeId())
                    .orElseThrow(() -> new ProjectNotFoundException(projectId));

            issue.setAssigneeId(request.getAssigneeId());
        }

        issue.setUpdatedAt(Instant.now());

        return IssueResponse.from(issueRepository.saveAndFlush(issue));
    }

}
