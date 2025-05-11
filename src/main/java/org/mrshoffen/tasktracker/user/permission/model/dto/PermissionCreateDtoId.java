package org.mrshoffen.tasktracker.user.permission.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.mrshoffen.tasktracker.commons.web.permissions.Permission;

import java.util.List;
import java.util.UUID;

public record PermissionCreateDtoId(
        @NotNull(message = "Id не может быть пустым")
        UUID userId,

        @NotEmpty(message = "Список разрешенных действий не должен быть пустым")
        List<Permission> permissions
) {
}
