package ru.glashiii.projectcoreservice.boards.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class BoardColumnUpdateRequest {
    @Size(min = 1, max = 100)
    private String name;
    private Integer position;
}
