package ru.glashiii.projectcoreservice.boards.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.glashiii.projectcoreservice.boards.BoardColumn;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class BoardColumnResponse {
    private Long id;
    private Long boardId;
    private Long projectId;
    private String name;
    private Integer position;
    private Instant createdAt;
    private Instant updatedAt;

    public static BoardColumnResponse from(BoardColumn boardColumn) {
        return new BoardColumnResponse(
                boardColumn.getId(),
                boardColumn.getBoardId(),
                boardColumn.getProjectId(),
                boardColumn.getName(),
                boardColumn.getPosition(),
                boardColumn.getCreatedAt(),
                boardColumn.getUpdatedAt()
        );
    }
}
