package ru.glashiii.projectcoreservice.issues;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.glashiii.projectcoreservice.issues.Issue;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    boolean existsByProjectIdAndTitle(Long projectId, String title);

    List<Issue> findAllByProjectIdOrderByIssueNumberAsc(Long projectId);

    Optional<Issue> findByIdAndProjectId(Long id, Long projectId);

    boolean existsByProjectIdAndColumnId(Long projectId, Long columnId);

    @Query("""
    select coalesce(max(i.position), 0)
    from Issue i
    where i.projectId = :projectId
      and i.columnId = :columnId
""")
    Integer findMaxPositionByProjectIdAndColumnId(
            @Param("projectId") Long projectId,
            @Param("columnId") Long columnId
    );

    List<Issue> findAllByProjectIdAndColumnIdOrderByPositionAsc(Long projectId, Long columnId);
}
