package ru.glashiii.projectcoreservice.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.glashiii.projectcoreservice.dto.ProjectCreateRequest;
import ru.glashiii.projectcoreservice.dto.ProjectResponse;
import ru.glashiii.projectcoreservice.entities.Project;
import ru.glashiii.projectcoreservice.entities.ProjectMember;
import ru.glashiii.projectcoreservice.entities.ProjectRole;
import ru.glashiii.projectcoreservice.repositories.ProjectMemberRepository;
import ru.glashiii.projectcoreservice.repositories.ProjectRepository;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public List<ProjectResponse> getMyProjects(Long currentUserId) {
        return projectRepository.findAllAvailableForUser(currentUserId)
                .stream()
                .map(ProjectResponse::from)
                .toList();
    }

    @Transactional
    public ProjectResponse createProject(ProjectCreateRequest projectCreateRequest,  Long currentUserId) {
        Instant now = Instant.now();
        Project projectToCreate = Project.builder()
                .name(projectCreateRequest.getName())
                .key(projectCreateRequest.getKey())
                .description(projectCreateRequest.getDescription())
                .createdAt(now)
                .updatedAt(now)
                .ownerId(currentUserId)
                .build();

        Project savedProject = projectRepository.save(projectToCreate);

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
