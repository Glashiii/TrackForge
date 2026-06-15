package ru.glashiii.projectcoreservice.boards;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(
        name = "board_columns",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_board_columns_board_position", columnNames = {"board_id", "position"})
        }
)
public class BoardColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Long boardId;
    @Column(nullable = false, updatable = false)
    private Long projectId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer position;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;
}
