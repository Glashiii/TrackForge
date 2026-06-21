package ru.glashiii.projectcoreservice.boards;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    List<BoardColumn> findAllByBoardIdOrderByPositionAsc(Long boardId);

    @Query("""
            select coalesce(max(c.position), 0)
                   from BoardColumn c
                            where c.boardId = :boardId
            """)
    Integer findMaxPositionByBoardId(@Param("boardId") Long boardId);

    Optional<BoardColumn> findByIdAndProjectId(Long columnId, Long projectId);

    boolean existsByBoardIdAndNameAndIdNot(Long boardId, String name, Long id);
}

