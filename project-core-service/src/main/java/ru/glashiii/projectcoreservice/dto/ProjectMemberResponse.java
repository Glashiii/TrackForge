package ru.glashiii.projectcoreservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.glashiii.projectcoreservice.entities.ProjectMember;
import ru.glashiii.projectcoreservice.entities.ProjectRole;

import java.time.Instant;
@Data
@AllArgsConstructor
public class ProjectMemberResponse {

    private Long id;
    private Long projectId;
    private Long userId;
    private ProjectRole role;
    private Instant joinedAt;

    public static ProjectMemberResponse from(ProjectMember projectMember){
        return new ProjectMemberResponse(
                projectMember.getId(),
                projectMember.getProjectId(),
                projectMember.getUserId(),
                projectMember.getRole(),
                projectMember.getJoinedAt()
        );
    }
}
