package org.mrshoffen.tasktracker.user.permission.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.mrshoffen.tasktracker.commons.web.permissions.Permission;

import java.util.List;

public record PermissionCreateDtoEmail(
        @Size(max = 128, min = 5, message = "Почта должна быть между 5 и 256 символами")
        @NotBlank(message = "Почта не может быть пустой")
        String userEmail,

        @NotEmpty(message = "Список разрешенных действий не должен быть пустым")
        List<Permission> permissions
) {
}
