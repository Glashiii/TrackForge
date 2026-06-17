package ru.glashiii.projectcoreservice.boards;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.glashiii.projectcoreservice.boards.dto.BoardColumnCreateRequest;
import ru.glashiii.projectcoreservice.boards.dto.BoardColumnResponse;
import ru.glashiii.projectcoreservice.boards.dto.BoardResponse;
import ru.glashiii.projectcoreservice.exceptions.DuplicateEntityParamException;
import ru.glashiii.projectcoreservice.exceptions.InvalidRequestDataException;
import ru.glashiii.projectcoreservice.exceptions.ProjectAccessDeniedException;
import ru.glashiii.projectcoreservice.exceptions.ProjectNotFoundException;
import ru.glashiii.projectcoreservice.issues.IssueRepository;
import ru.glashiii.projectcoreservice.projects.ProjectMember;
import ru.glashiii.projectcoreservice.projects.ProjectMemberRepository;
import ru.glashiii.projectcoreservice.projects.ProjectRepository;
import ru.glashiii.projectcoreservice.projects.ProjectRole;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final IssueRepository issueRepository;

    @Transactional
    public void createDefaultBoard(Long projectId) {

        if (boardRepository.existsByProjectId(projectId)) {
            throw new DuplicateEntityParamException("Project with id " + projectId + " already have board");
        }

        if (projectRepository.findById(projectId).isEmpty()) {
            throw new ProjectNotFoundException(projectId);
        }

        Instant now = Instant.now();

        Board board = Board.builder()
                .projectId(projectId)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Board savedBoard = boardRepository.save(board);

        List<BoardColumn> defaultColumns = List.of(
                createColumn(savedBoard, "To Do", 1, now),
                createColumn(savedBoard, "In Progress", 2, now),
                createColumn(savedBoard, "Done", 3, now),
                createColumn(savedBoard, "Cancelled", 4, now)
        );

        boardColumnRepository.saveAll(defaultColumns);

    }

    @Transactional(readOnly = true)
    public BoardResponse getBoardByProjectId(Long userId, Long projectId) {
        projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (projectRepository.findById(projectId).isEmpty()) {
            throw new ProjectNotFoundException(projectId);
        }
        // TODO: exception for board
        Board board = boardRepository.findByProjectId(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));
        List<BoardColumnResponse> columns = boardColumnRepository.findAllByBoardIdOrderByPositionAsc(board.getId()).stream().map(
                c -> BoardColumnResponse.from(c)
        ).toList();

        return BoardResponse.from(board, columns);
    }

    @Transactional
    public BoardColumnResponse createColumnInProject(Long userId, Long projectId, BoardColumnCreateRequest request) {
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (projectMember.getRole() != ProjectRole.OWNER) {
            throw new ProjectAccessDeniedException(projectId);
        }

        Board board = boardRepository.findByProjectIdForUpdate(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        Integer nextPos = boardColumnRepository.findMaxPositionByBoardId(board.getId()) + 1;

        Instant now = Instant.now();

        BoardColumn column = BoardColumn.builder()
                .boardId(board.getId())
                .projectId(projectId)
                .name(request.getName().trim())
                .position(nextPos)
                .createdAt(now)
                .updatedAt(now)
                .build();

        board.setUpdatedAt(now);

        BoardColumn savedColumn = boardColumnRepository.save(column);

        return BoardColumnResponse.from(savedColumn);
    }

    @Transactional
    public void deleteColumn(Long projectId, Long columnId, Long userId) {
        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (projectMember.getRole() != ProjectRole.OWNER) {
            throw new ProjectAccessDeniedException(projectId);
        }

        Board board = boardRepository.findByProjectIdForUpdate(projectId).orElseThrow(() -> new ProjectNotFoundException(projectId));

        BoardColumn column = boardColumnRepository.findByIdAndProjectId(columnId, projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (issueRepository.existsByProjectIdAndColumnId(projectId, columnId)) {
            throw new InvalidRequestDataException("Cannot delete column with issues");
        }

        boardColumnRepository.delete(column);
        boardColumnRepository.flush();

        recalculateColumnPositions(board.getId());

        Instant now = Instant.now();
        board.setUpdatedAt(now);

    }


    private void recalculateColumnPositions(Long boardId) {
        List<BoardColumn> columns = boardColumnRepository.findAllByBoardIdOrderByPositionAsc(boardId);

        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).setPosition(i + 1);
        }
        boardColumnRepository.saveAll(columns);
    }

    private BoardColumn createColumn(Board board, String name, Integer position, Instant now) {
        return BoardColumn.builder()
                .boardId(board.getId())
                .projectId(board.getProjectId())
                .name(name)
                .position(position)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
