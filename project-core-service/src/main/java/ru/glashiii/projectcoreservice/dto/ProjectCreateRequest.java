package ru.glashiii.projectcoreservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectCreateRequest {

    @NotBlank
    private String name;

    @Size(max = 255)
    private String description;

    @NotBlank
    private String key;

}
