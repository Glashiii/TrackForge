package ru.glashiii.projectcoreservice.boards.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.glashiii.projectcoreservice.boards.Board;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class BoardResponse {
    private Long id;
    private Long projectId;
    private Instant createdAt;
    private Instant updatedAt;

    private List<BoardColumnResponse> columns;

    public static BoardResponse from(Board board, List<BoardColumnResponse> columns) {
        return new BoardResponse(
                board.getId(),
                board.getProjectId(),
                board.getCreatedAt(),
                board.getUpdatedAt(),
                columns
                );
    }
}


