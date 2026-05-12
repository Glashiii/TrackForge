package ru.glashiii.projectcoreservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.glashiii.projectcoreservice.entities.ProjectRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberUpdateRequest {
    @NotNull
    private ProjectRole role;

}
