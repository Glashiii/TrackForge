package ru.glashiii.projectcoreservice.projects.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.glashiii.projectcoreservice.projects.ProjectRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberUpdateRequest {
    @NotNull
    private ProjectRole role;

}
