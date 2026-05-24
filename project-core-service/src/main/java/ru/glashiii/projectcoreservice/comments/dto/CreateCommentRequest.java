package ru.glashiii.projectcoreservice.comments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreateCommentRequest {
    @NotBlank
    @Size(min = 1, max = 1024)
    private String body;
}
