package ru.glashiii.projectcoreservice.issues.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.glashiii.projectcoreservice.issues.IssuePriority;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueMoveRequest {
    @NotNull
    private Integer position;

    @NotNull
    private Long columnId;
}
