package ru.glashiii.projectcoreservice.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.glashiii.projectcoreservice.entities.ProjectMember;

import java.util.Optional;

public interface ProjectMemberRepository extends CrudRepository<ProjectMember, Long> {
    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, Long userId);

    void deleteByProjectId(Long projectId);
}
