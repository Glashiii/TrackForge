package ru.glashiii.projectcoreservice.issues;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
