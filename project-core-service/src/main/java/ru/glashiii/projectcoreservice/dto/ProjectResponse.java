package ru.glashiii.projectcoreservice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.glashiii.projectcoreservice.entities.Project;

import java.time.Instant;

@Data
@AllArgsConstructor
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private String key;
    private Long ownerId;
    private Instant createdAt;
    private Instant updatedAt;

    public static ProjectResponse from(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getKey(),
                project.getOwnerId(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
