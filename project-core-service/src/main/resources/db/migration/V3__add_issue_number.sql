DO $$
BEGIN
    IF to_regclass('public.issue') IS NOT NULL AND to_regclass('public.issues') IS NULL THEN
        ALTER TABLE issue RENAME TO issues;
    END IF;
END $$;

ALTER TABLE projects
    ADD COLUMN IF NOT EXISTS next_issue_number BIGINT NOT NULL DEFAULT 1;

ALTER TABLE issues
    ADD COLUMN IF NOT EXISTS issue_number BIGINT;

WITH numbered AS (
    SELECT id, row_number() OVER (PARTITION BY project_id ORDER BY id) AS issue_number
    FROM issues
    WHERE issue_number IS NULL
)
UPDATE issues
SET issue_number = numbered.issue_number
FROM numbered
WHERE issues.id = numbered.id;

ALTER TABLE issues
    ALTER COLUMN issue_number SET NOT NULL;

DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'issues'
          AND column_name = 'type'
          AND data_type = 'smallint'
    ) THEN
        ALTER TABLE issues
            ALTER COLUMN type TYPE VARCHAR(20)
            USING CASE type
                WHEN 0 THEN 'TASK'
                WHEN 1 THEN 'BUG'
                WHEN 2 THEN 'STORY'
            END;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'issues'
          AND column_name = 'status'
          AND data_type = 'smallint'
    ) THEN
        ALTER TABLE issues
            ALTER COLUMN status TYPE VARCHAR(20)
            USING CASE status
                WHEN 0 THEN 'TODO'
                WHEN 1 THEN 'IN_PROGRESS'
                WHEN 2 THEN 'DONE'
                WHEN 3 THEN 'CANCELLED'
            END;
    END IF;

    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'public'
          AND table_name = 'issues'
          AND column_name = 'priority'
          AND data_type = 'smallint'
    ) THEN
        ALTER TABLE issues
            ALTER COLUMN priority TYPE VARCHAR(20)
            USING CASE priority
                WHEN 0 THEN 'LOW'
                WHEN 1 THEN 'MEDIUM'
                WHEN 2 THEN 'HIGH'
            END;
    END IF;
END $$;

UPDATE projects
SET next_issue_number = issue_numbers.next_issue_number
FROM (
    SELECT projects.id, COALESCE(MAX(issues.issue_number), 0) + 1 AS next_issue_number
    FROM projects
    LEFT JOIN issues ON issues.project_id = projects.id
    GROUP BY projects.id
) issue_numbers
WHERE projects.id = issue_numbers.id
  AND projects.next_issue_number < issue_numbers.next_issue_number;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_issues_project'
    ) THEN
        ALTER TABLE issues
            ADD CONSTRAINT fk_issues_project FOREIGN KEY (project_id) REFERENCES projects (id);
    END IF;

    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uk_issues_project_issue_number'
    ) THEN
        ALTER TABLE issues
            ADD CONSTRAINT uk_issues_project_issue_number UNIQUE (project_id, issue_number);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_issues_project_id_created_at
    ON issues (project_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_issues_assignee_id
    ON issues (assignee_id);
