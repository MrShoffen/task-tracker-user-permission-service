package org.mrshoffen.tasktracker.user.permission.controller;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.commons.web.dto.UserPermissionResponseDto;
import org.mrshoffen.tasktracker.commons.web.permissions.Permission;
import org.mrshoffen.tasktracker.user.permission.service.PermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/permissions")
public class InternalPermissionsController {

    private final PermissionService permissionService;

    @GetMapping("/users/{userId}/workspaces/{workspaceId}")
    Flux<Permission> getUserPermissionsInWorkspace(@PathVariable("userId") UUID userId,
                                                   @PathVariable("workspaceId") UUID workspaceId) {
        return permissionService
                .getUserPermissionsInWorkspace(userId, workspaceId);
    }

    @GetMapping("/workspaces/{workspaceId}")
    Flux<UserPermissionResponseDto> getAllPermissionsInWorkspace(@PathVariable("workspaceId") UUID workspaceId) {
        return permissionService
                .getAllPermissionsInWorkspace(workspaceId);
    }
}
