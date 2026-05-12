package ru.glashiii.projectcoreservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.glashiii.projectcoreservice.dto.*;
import ru.glashiii.projectcoreservice.entities.Project;
import ru.glashiii.projectcoreservice.entities.ProjectMember;
import ru.glashiii.projectcoreservice.entities.ProjectRole;
import ru.glashiii.projectcoreservice.exceptions.*;
import ru.glashiii.projectcoreservice.repositories.ProjectMemberRepository;
import ru.glashiii.projectcoreservice.repositories.ProjectRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional(readOnly = true)
    public List<ProjectResponse> getMyProjects(Long currentUserId) {
        return projectRepository.findAllAvailableForUser(currentUserId)
                .stream()
                .map(ProjectResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long projectId, Long currentUserId) {
        if (!projectMemberRepository.existsByProjectIdAndUserId(projectId, currentUserId)) {
            throw new ProjectNotFoundException(projectId);
        }
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        return ProjectResponse.from(project);
    }

    @Transactional
    public ProjectResponse createProject(ProjectCreateRequest projectCreateRequest, Long currentUserId) {
        String projectKey = projectCreateRequest.getKey();
        if (projectRepository.existsByKey(projectKey)) {
            throw new DuplicateProjectKeyException(projectKey);
        }

        Instant now = Instant.now();
        Project projectToCreate = Project.builder()
                .name(projectCreateRequest.getName())
                .key(projectCreateRequest.getKey())
                .description(projectCreateRequest.getDescription())
                .createdAt(now)
                .updatedAt(now)
                .ownerId(currentUserId)
                .build();

        Project savedProject;

        try {
            savedProject = projectRepository.saveAndFlush(projectToCreate);


        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateProjectKeyException(projectKey);
        }

        ProjectMember ownerMember = ProjectMember.builder()
                .projectId(savedProject.getId())
                .userId(currentUserId)
                .role(ProjectRole.OWNER)
                .joinedAt(now)
                .build();

        projectMemberRepository.save(ownerMember);

        return ProjectResponse.from(savedProject);
    }

    @Transactional
    public ProjectResponse updateProject(Long projectId, ProjectUpdateRequest projectUpdateRequest, Long currentUserId) {

        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndUserId(projectId, currentUserId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (projectMember.getRole() != ProjectRole.OWNER) {
            throw new ProjectAccessDeniedException(projectId);
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (projectUpdateRequest.getName() != null) {
            if (projectUpdateRequest.getName().isBlank()) {
                throw new InvalidRequestDataException("Name cannot be empty");
            }

            project.setName(projectUpdateRequest.getName().trim());
        }

        if (projectUpdateRequest.getDescription() != null) {
            project.setDescription(projectUpdateRequest.getDescription().trim());
        }

        project.setUpdatedAt(Instant.now());

        return ProjectResponse.from(projectRepository.save(project));
    }

    @Transactional
    public void deleteProject(Long projectId, Long currentUserId) {

        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, currentUserId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (member.getRole() != ProjectRole.OWNER) {
            throw new ProjectAccessDeniedException(projectId);
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        projectMemberRepository.deleteByProjectId(projectId);
        projectRepository.delete(project);

    }

    @Transactional
    public ProjectMemberResponse createMember(Long currentUserId, Long projectId, ProjectMemberCreateRequest request) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        ProjectMember owner = projectMemberRepository.findByProjectIdAndUserId(projectId, currentUserId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (owner.getRole() != ProjectRole.OWNER) {
            throw new ProjectAccessDeniedException(projectId);
        }

        if (request.getRole() == ProjectRole.OWNER) {
            throw new InvalidRequestDataException("Cannot create another owner of project");
        }

        if (projectMemberRepository.findByProjectIdAndUserId(projectId, request.getUserId()).isPresent()) {
            throw new MemberAlreadyExists(projectId, request.getUserId());
        }
        ProjectMember newMember = ProjectMember.builder()
                .projectId(project.getId())
                .userId(request.getUserId())
                .joinedAt(Instant.now())
                .role(request.getRole()).build();

        try {
            ProjectMember saved = projectMemberRepository.saveAndFlush(newMember);
            return ProjectMemberResponse.from(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateEntityParamException("Cannot create same member of project");
        }
    }


    @Transactional(readOnly = true)
    public List<ProjectMemberResponse> getProjectMembers(Long currentUserId, Long projectId) {
        if (projectMemberRepository.findByProjectIdAndUserId(projectId, currentUserId).isEmpty()) {
            throw new ProjectNotFoundException(projectId);
        }

        return projectMemberRepository.findAllByProjectId(projectId)
                .stream()
                .map(ProjectMemberResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectMemberResponse getMemberInProject(Long projectId, Long userId, Long currentUserId) {
        projectMemberRepository.findByProjectIdAndUserId(projectId, currentUserId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        return ProjectMemberResponse.from(member);

    }
}
