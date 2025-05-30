package org.mrshoffen.tasktracker.user.permission.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.commons.web.dto.UserPermissionResponseDto;
import org.mrshoffen.tasktracker.user.permission.model.dto.PermissionCreateDtoEmail;
import org.mrshoffen.tasktracker.user.permission.model.dto.PermissionCreateDtoId;
import org.mrshoffen.tasktracker.user.permission.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mrshoffen.tasktracker.commons.web.authentication.AuthenticationAttributes.AUTHORIZED_USER_HEADER_NAME;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces/{workspaceId}/permissions")
public class ExternalPermissionsController {

    private final PermissionService permissionService;

    @PostMapping("/by-email")
    Mono<ResponseEntity<UserPermissionResponseDto>> grantPermissionsToUserByEmail(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID principalId,
                                                                                  @PathVariable("workspaceId") UUID workspaceId,
                                                                                  @Valid @RequestBody Mono<PermissionCreateDtoEmail> createDto) {
        return createDto
                .flatMap(dto ->
                        permissionService
                                .grantPermissionsToUserByEmail(principalId, dto, workspaceId))
                .map(permission -> ResponseEntity.status(CREATED).body(permission)
                );
    }

    @PostMapping("/by-id")
    Mono<ResponseEntity<UserPermissionResponseDto>> grantPermissionsToUserById(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID principalId,
                                                                               @PathVariable("workspaceId") UUID workspaceId,
                                                                               @Valid @RequestBody Mono<PermissionCreateDtoId> createDto) {
        return createDto
                .flatMap(dto ->
                        permissionService
                                .grantPermissionsToUserById(principalId, dto, workspaceId))
                .map(permission -> ResponseEntity.status(CREATED).body(permission)
                );

    }

    @DeleteMapping("/{userId}")
    Mono<Void> revokeUserPermissions(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID principalId,
                                     @PathVariable("workspaceId") UUID workspaceId,
                                     @PathVariable("userId") UUID userId) {
        return permissionService
                .revokeUserPermissions(principalId, userId, workspaceId);
    }

    @GetMapping
    Flux<UserPermissionResponseDto> getAllPermissionsInWorkspace(@RequestHeader(AUTHORIZED_USER_HEADER_NAME) UUID principalId,
                                                                 @PathVariable("workspaceId") UUID workspaceId) {
        return permissionService
                .getAllPermissions(principalId, workspaceId);
    }

}
