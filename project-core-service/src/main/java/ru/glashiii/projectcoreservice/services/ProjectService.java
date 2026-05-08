package ru.glashiii.projectcoreservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.glashiii.projectcoreservice.dto.ProjectCreateRequest;
import ru.glashiii.projectcoreservice.dto.ProjectResponse;
import ru.glashiii.projectcoreservice.entities.Project;
import ru.glashiii.projectcoreservice.entities.ProjectMember;
import ru.glashiii.projectcoreservice.entities.ProjectRole;
import ru.glashiii.projectcoreservice.exceptions.DuplicateProjectKeyException;
import ru.glashiii.projectcoreservice.exceptions.ProjectNotFoundException;
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
        if (!projectMemberRepository.existsByProjectIdAndUserId(projectId,currentUserId)){
            throw new ProjectNotFoundException(projectId);
        }
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        return ProjectResponse.from(project);
    }

    @Transactional
    public ProjectResponse createProject(ProjectCreateRequest projectCreateRequest,  Long currentUserId) {
        String projectKey = projectCreateRequest.getKey();
        if (projectRepository.existsByKey(projectKey)){
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


}
