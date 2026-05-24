package ru.glashiii.projectcoreservice.projects;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.glashiii.projectcoreservice.projects.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("""
        select p from Project p\s
        where p.id in (select pm.projectId
            from ProjectMember pm where pm.userId = :userId)
        order by p.createdAt desc
       \s""")
    List<Project> findAllAvailableForUser(@Param("userId") Long userId);

    boolean existsByKey(String key);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Project p where p.id = :id")
    Optional<Project> findByIdForUpdate(@Param("id") Long projectId);
}
