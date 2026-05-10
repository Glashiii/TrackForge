package ru.glashiii.projectcoreservice.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.glashiii.projectcoreservice.dto.ProjectCreateRequest;
import ru.glashiii.projectcoreservice.dto.ProjectResponse;
import ru.glashiii.projectcoreservice.dto.ProjectUpdateRequest;
import ru.glashiii.projectcoreservice.security.CurrentUserProvider;
import ru.glashiii.projectcoreservice.services.ProjectService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getMyProjects() {
        Long currentUserId = currentUserProvider.getCurrentUserId();

        List<ProjectResponse> projects = projectService.getMyProjects(currentUserId);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable Long id){
        Long currentUserId = currentUserProvider.getCurrentUserId();

        ProjectResponse pr = projectService.getProjectById(id, currentUserId);

        return ResponseEntity.ok(pr);
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectCreateRequest projectCreateRequest) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        ProjectResponse response = projectService.createProject(projectCreateRequest, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectUpdateRequest projectUpdateRequest) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        ProjectResponse response = projectService.updateProject(id, projectUpdateRequest, currentUserId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        projectService.deleteProject(id, currentUserId);

        return ResponseEntity.noContent().build();
    }
}
