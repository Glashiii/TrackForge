package ru.glashiii.projectcoreservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glashiii.projectcoreservice.entities.Issue;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    boolean existsByProjectIdAndTitle(Long projectId, String title);
}
