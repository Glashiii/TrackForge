package ru.glashiii.projectcoreservice.issues;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.glashiii.projectcoreservice.boards.Board;
import ru.glashiii.projectcoreservice.boards.BoardColumn;
import ru.glashiii.projectcoreservice.boards.BoardColumnRepository;
import ru.glashiii.projectcoreservice.boards.BoardRepository;
import ru.glashiii.projectcoreservice.issues.dto.IssueCreateRequest;
import ru.glashiii.projectcoreservice.issues.dto.IssueMoveRequest;
import ru.glashiii.projectcoreservice.issues.dto.IssueResponse;
import ru.glashiii.projectcoreservice.issues.dto.IssueUpdateRequest;
import ru.glashiii.projectcoreservice.issues.*;
import ru.glashiii.projectcoreservice.projects.Project;
import ru.glashiii.projectcoreservice.projects.ProjectMember;
import ru.glashiii.projectcoreservice.projects.ProjectRole;
import ru.glashiii.projectcoreservice.exceptions.*;
import ru.glashiii.projectcoreservice.issues.IssueRepository;
import ru.glashiii.projectcoreservice.projects.ProjectMemberRepository;
import ru.glashiii.projectcoreservice.projects.ProjectRepository;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final BoardRepository boardRepository;
    private final BoardColumnRepository boardColumnRepository;

    @Transactional
    public IssueResponse createIssue(Long userId, Long projectId, IssueCreateRequest issueCreateRequest) {
        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (member.getRole() == ProjectRole.VIEWER) {
            throw new IssueAccessDeniedException(userId, projectId);
        }

        Project project = projectRepository.findByIdForUpdate(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        Long issueNumber = project.getNextIssueNumber();

        project.setNextIssueNumber(issueNumber + 1L);


        if (issueCreateRequest.getAssigneeId() != null
                && !projectMemberRepository.existsByProjectIdAndUserId(projectId, issueCreateRequest.getAssigneeId())) {
            throw new InvalidRequestDataException("Assignee must be a project member");
        }

        Board board = boardRepository.findByProjectIdForUpdate(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        BoardColumn targetColumn = boardColumnRepository.findAllByBoardIdOrderByPositionAsc(board.getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new InvalidRequestDataException("Project board has no columns"));

        Integer position = issueRepository.findMaxPositionByProjectIdAndColumnId(
                projectId,
                targetColumn.getId()
        ) + 1;

        Issue issue = Issue.builder()
                .projectId(projectId)
                .type(issueCreateRequest.getIssueType())
                .title(issueCreateRequest.getTitle())
                .description(issueCreateRequest.getDescription())
                .priority(issueCreateRequest.getPriority())
                .assigneeId(issueCreateRequest.getAssigneeId())
                .reporterId(userId)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .issueNumber(issueNumber)
                .columnId(targetColumn.getId())
                .position(position)
                .build();

        try {
            Issue saved = issueRepository.saveAndFlush(issue);
            return IssueResponse.from(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntityParamException("Cannot create same issue");
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

        if (member.getRole() == ProjectRole.VIEWER) {
            throw new IssueAccessDeniedException(userId, projectId);
        }

        if (!Objects.equals(issue.getReporterId(), userId) && member.getRole() != ProjectRole.OWNER) {
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

        if (member.getRole() == ProjectRole.VIEWER) {
            throw new IssueAccessDeniedException(userId, projectId);
        }

        if (!Objects.equals(issue.getReporterId(), userId) && member.getRole() != ProjectRole.OWNER) {
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

    @Transactional
    public IssueResponse moveIssue(Long userId, Long projectId, Long issueId, IssueMoveRequest request) {

        boardRepository.findByProjectIdForUpdate(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        BoardColumn targetColumn = boardColumnRepository.findByIdAndProjectId(request.getColumnId(), projectId)
                .orElseThrow(() -> new InvalidRequestDataException("Column not found"));



        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        Issue targetIssue = issueRepository.findByIdAndProjectId(issueId, projectId).orElseThrow(
                () -> new IssueNotFoundException(issueId));

        boolean sameColumn = Objects.equals(targetIssue.getColumnId(), targetColumn.getId());

        List<Issue> issues = issueRepository.findAllByProjectIdAndColumnIdOrderByPositionAsc(projectId, request.getColumnId());


        Integer targetPosition = request.getPosition();

        if (member.getRole() == ProjectRole.VIEWER) {
            throw new IssueAccessDeniedException(userId, projectId);
        }

        if (!Objects.equals(targetIssue.getReporterId(), userId) && member.getRole() != ProjectRole.OWNER) {
            throw new IssueAccessDeniedException(userId, projectId);
        }

        int maxPosition = sameColumn ? issues.size() : issues.size() + 1;

        if (targetPosition < 1 || targetPosition > maxPosition) {
            throw new InvalidRequestDataException("Target position is out of range");
        }


        // if move to different column flow
        if (sameColumn) {

            issues.removeIf(issue -> issue.getId().equals(targetIssue.getId()));
        } else {
            List<Issue> sourceColumnIssues = issueRepository.findAllByProjectIdAndColumnIdOrderByPositionAsc(
                    projectId,
                    targetIssue.getColumnId()
            );

            sourceColumnIssues.removeIf(issue -> issue.getId().equals(targetIssue.getId()));
            recalculateColumn(sourceColumnIssues);

            targetIssue.setColumnId(targetColumn.getId());
        }
        issues.add(targetPosition - 1, targetIssue);
        recalculateColumn(issues);

        targetIssue.setUpdatedAt(Instant.now());

        return IssueResponse.from(targetIssue);
    }


    private void recalculateColumn(List<Issue> issues) {

        for (int i = 0; i < issues.size(); i++) {
            issues.get(i).setPosition(-(i + 1));
        }

        issueRepository.flush();

        for (int i = 0; i < issues.size(); i++) {
            issues.get(i).setPosition((i + 1));
        }
    }
}
