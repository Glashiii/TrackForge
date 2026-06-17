package ru.glashiii.projectcoreservice.boards;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    boolean existsByProjectId(Long projectId);

    Optional<Board> findByProjectId(Long projectId);

    @Query("select b from Board b where b.projectId = :projectId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Board> findByProjectIdForUpdate(@Param("projectId") Long projectId);

}
