--liquibase formatted sql


--changeset mrshoffen:2
CREATE TABLE IF NOT EXISTS users_permissions
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      UUID NOT NULL,
    workspace_id UUID NOT NULL,
    permissions  VARCHAR(650)
);

CREATE INDEX user_id_workspace_id_index ON users_permissions (user_id, workspace_id);