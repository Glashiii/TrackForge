package ru.glashiii.projectcoreservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.glashiii.projectcoreservice.entities.ProjectRole;

@Data
public class ProjectMemberCreateRequest {

    @NotNull
    @Positive
    private Long userId;

    @NotNull
    private ProjectRole role;
}
