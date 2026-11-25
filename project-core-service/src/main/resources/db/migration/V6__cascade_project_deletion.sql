ALTER TABLE boards
    DROP CONSTRAINT fk_boards_project,
    ADD CONSTRAINT fk_boards_project
        FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE;

ALTER TABLE board_columns
    DROP CONSTRAINT fk_board_columns_board,
    DROP CONSTRAINT fk_board_columns_project,
    ADD CONSTRAINT fk_board_columns_board
        FOREIGN KEY (board_id) REFERENCES boards (id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_board_columns_project
        FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE;

ALTER TABLE issues
    DROP CONSTRAINT fk_issues_project,
    DROP CONSTRAINT fk_issues_column_project,
    ADD CONSTRAINT fk_issues_project
        FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_issues_column_project
        FOREIGN KEY (column_id, project_id) REFERENCES board_columns (id, project_id) ON DELETE CASCADE;

ALTER TABLE comments
    DROP CONSTRAINT fk_comments_project,
    DROP CONSTRAINT fk_comments_issue,
    ADD CONSTRAINT fk_comments_project
        FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_comments_issue
        FOREIGN KEY (issue_id) REFERENCES issues (id) ON DELETE CASCADE;
