package ru.glashiii.projectcoreservice.projects.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectUpdateRequest {

    @Size(max = 100)
    private String name;

    @Size(max = 255)
    private String description;
}
