package ru.glashiii.projectcoreservice.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.glashiii.projectcoreservice.entities.ProjectMember;

public interface ProjectMemberRepository extends CrudRepository<ProjectMember, Long> {
}
