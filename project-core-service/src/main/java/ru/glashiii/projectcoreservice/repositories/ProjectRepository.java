package ru.glashiii.projectcoreservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.glashiii.projectcoreservice.entities.Project;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("""
        select p from Project p\s
        where p.id in (select pm.projectId
            from ProjectMember pm where pm.userId = :userId)
        order by p.createdAt desc
       \s""")
    List<Project> findAllAvailableForUser(@Param("userId") Long userId);

    boolean existsByKey(String key);
}
