package ru.glashiii.projectcoreservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    public Long id;

    @Column(nullable = false)
    public String name;

    public String description;

    @Column(nullable = false, unique = true, length = 10)
    public String key;

    @Column(nullable = false)
    public Long ownerId;

    private Instant createdAt;

    private Instant updatedAt;
}

