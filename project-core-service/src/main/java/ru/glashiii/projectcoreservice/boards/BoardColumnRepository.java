package ru.glashiii.projectcoreservice.boards;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
    List<BoardColumn> findAllByBoardIdOrderByPositionAsc(Long boardId);

    @Query("""
            select coalesce(max(c.position), 0)
                   from BoardColumn c
                            where c.boardId = :boardId
            """)
    Integer findMaxPositionByBoardId(@Param("boardId") Long boardId);
}
