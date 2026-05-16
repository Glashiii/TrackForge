package ru.glashiii.projectcoreservice.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;

import java.time.Instant;

@Entity
public class Issues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long projectId;

    @Column(nullable = false)
    private IssueType type;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    private IssueStatus status;

    @Column(nullable = false)
    private IssuePriority priority;

    @Column
    private Long assigneeId;

    @Column(nullable = false, updatable = false)
    private Long reporterId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column
    private Instant updatedAt;
}
