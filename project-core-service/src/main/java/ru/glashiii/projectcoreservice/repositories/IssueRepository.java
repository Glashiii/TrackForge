package ru.glashiii.projectcoreservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glashiii.projectcoreservice.entities.Issue;

import java.util.List;
import java.util.Optional;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    boolean existsByProjectIdAndTitle(Long projectId, String title);

    List<Issue> findAllByProjectIdOrderByIssueNumberAsc(Long projectId);

    Optional<Issue> findByIdAndProjectId(Long id, Long projectId);

}
