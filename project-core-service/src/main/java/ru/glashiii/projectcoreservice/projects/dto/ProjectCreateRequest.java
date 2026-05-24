package ru.glashiii.projectcoreservice.projects.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectCreateRequest {

    @NotBlank
    private String name;

    @Size(max = 255)
    private String description;

    @NotBlank
    @Size(max = 10)
    @Pattern(regexp = "^[A-Z][A-Z0-9]{1,9}$")
    private String key;

}
