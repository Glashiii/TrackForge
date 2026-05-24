package ru.glashiii.projectcoreservice.projects.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.glashiii.projectcoreservice.projects.ProjectRole;

@Data
public class ProjectMemberCreateRequest {

    @NotNull
    @Positive
    private Long userId;

    @NotNull
    private ProjectRole role;
}
