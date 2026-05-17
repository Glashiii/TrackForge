package ru.glashiii.projectcoreservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// TODO other unique constraints
@Table(name = "projects",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_projects_key", columnNames = "key")
        })
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long nextIssueNumber;

    @Column(nullable = false)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 10)
    private String key;

    @Column(nullable = false)
    private Long ownerId;

    private Instant createdAt;

    private Instant updatedAt;
}

