package ru.glashiii.projectcoreservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.glashiii.projectcoreservice.entities.ProjectMember;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, Long userId);

    void deleteByProjectId(Long projectId);

    List<ProjectMember> findAllByProjectId(Long projectId);
}
