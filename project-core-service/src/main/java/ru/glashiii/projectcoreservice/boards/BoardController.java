package ru.glashiii.projectcoreservice.boards;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.glashiii.projectcoreservice.boards.dto.BoardColumnCreateRequest;
import ru.glashiii.projectcoreservice.boards.dto.BoardColumnResponse;
import ru.glashiii.projectcoreservice.boards.dto.BoardResponse;
import ru.glashiii.projectcoreservice.security.CurrentUserProvider;

@RestController
@AllArgsConstructor
@RequestMapping("/projects/{projectId}")
public class BoardController {

    private final BoardService boardService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping("/board")
    public ResponseEntity<BoardResponse> getBoardByProject(@PathVariable Long projectId) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        BoardResponse board = boardService.getBoardByProjectId(currentUserId, projectId);
        return ResponseEntity.ok(board);
    }

    @PostMapping("/board/columns")
    public ResponseEntity<BoardColumnResponse> createBoardColumn(
            @PathVariable Long projectId,
            @Valid @RequestBody BoardColumnCreateRequest request) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        BoardColumnResponse column = boardService.createColumnInProject(currentUserId, projectId, request);
        return ResponseEntity.ok(column);
    }


    @DeleteMapping("/board/columns/{columnId}")
    public ResponseEntity<Void> deleteBoardColumnByProject(
            @PathVariable Long projectId,
            @PathVariable Long columnId
    ) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        boardService.deleteColumn(projectId, columnId, currentUserId);
        return ResponseEntity.noContent().build();
    }

}
