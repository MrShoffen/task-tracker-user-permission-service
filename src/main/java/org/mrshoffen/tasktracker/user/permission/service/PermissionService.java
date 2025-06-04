package org.mrshoffen.tasktracker.user.permission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mrshoffen.tasktracker.commons.web.dto.UserPermissionResponseDto;
import org.mrshoffen.tasktracker.commons.web.exception.AccessDeniedException;
import org.mrshoffen.tasktracker.commons.web.permissions.Permission;
import org.mrshoffen.tasktracker.user.permission.client.UserClient;
import org.mrshoffen.tasktracker.user.permission.mapper.UserPermissionMapper;
import org.mrshoffen.tasktracker.user.permission.model.dto.PermissionCreateDtoEmail;
import org.mrshoffen.tasktracker.user.permission.model.dto.PermissionCreateDtoId;
import org.mrshoffen.tasktracker.user.permission.model.entity.UserPermission;
import org.mrshoffen.tasktracker.user.permission.repository.UserPermissionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class PermissionService {

    private final UserPermissionRepository userPermissionRepository;

    private final UserClient userClient;

    private final UserPermissionMapper userPermissionMapper;

    public Mono<Void> grantAllPermissionsToWorkspaceOwner(UUID userId, UUID workspaceId) {
        return Flux.fromArray(Permission.values())
                .collectList()
                .flatMap(permissions -> {
                    UserPermission userPermission = new UserPermission();
                    userPermission.setUserId(userId);
                    userPermission.setWorkspaceId(workspaceId);
                    userPermission.setPermissions(permissions);
                    return userPermissionRepository.save(userPermission);
                })
                .then();
    }

   public Flux<UserPermissionResponseDto> getAllUserPermissions(UUID userId) {
        return userPermissionRepository
                .findAllByUserId(userId)
                .map(userPermissionMapper::toDto);
    }

    public Mono<UserPermissionResponseDto> grantPermissionsToUserByEmail(UUID principalId, PermissionCreateDtoEmail createDto, UUID workspaceId) {
        return verifyPrincipalPermissionToGrant(principalId, workspaceId)
                .then(userClient.getUserId(createDto.userEmail()))
                .flatMap(userId ->
                        grantPermissionsNoPrincipalCheck(userId, workspaceId, createDto.permissions()));

    }

    public Mono<UserPermissionResponseDto> grantPermissionsToUserById(UUID principalId, PermissionCreateDtoId createDto, UUID workspaceId) {
        return verifyPrincipalPermissionToGrant(principalId, workspaceId)
                .then(Mono.just(createDto.userId()))
                .flatMap(userId ->
                        grantPermissionsNoPrincipalCheck(userId, workspaceId, createDto.permissions()));

    }

    public Flux<UserPermissionResponseDto> getAllPermissionsInWorkspace(UUID principalId, UUID workspaceId) {
        return getUserPermissionsInWorkspace(principalId, workspaceId)
                .collectList()
                .flatMapMany(permissions -> {
                    if (permissions.isEmpty()) {
                        return Flux.error(new AccessDeniedException(
                                "Данный пользователь не может выдавать права в пространстве!"));
                    } else {
                        return userPermissionRepository
                                .findAllByWorkspaceId(workspaceId);
                    }
                })
                .map(userPermissionMapper::toDto);
    }

    public Flux<UserPermissionResponseDto> getAllPermissionsInWorkspace(UUID workspaceId) {
        return userPermissionRepository
                .findAllByWorkspaceId(workspaceId)
                .map(userPermissionMapper::toDto);
    }

    private Mono<Void> verifyPrincipalPermissionToGrant(UUID principalId, UUID workspaceId) {
        return getUserPermissionsInWorkspace(principalId, workspaceId)
                .collectList()
                .flatMap(permissions -> {
                    if (permissions.contains(Permission.UPDATE_WORKSPACE_PERMISSIONS)) {
                        return Mono.empty();
                    } else {
                        return Mono.error(new AccessDeniedException("Данный пользователь не может выдавать права в пространстве!"));
                    }
                });
    }

    private Mono<UserPermissionResponseDto> grantPermissionsNoPrincipalCheck(UUID userId, UUID workspaceId, List<Permission> permissions) {
        UserPermission userPermission = userPermissionMapper.toEntity(userId, workspaceId, permissions);

        return userPermissionRepository
                .findByUserIdAndWorkspaceId(userId, workspaceId)
                .switchIfEmpty(Mono.just(userPermission))
                .flatMap(userPerms -> {
                    userPerms.setPermissions(permissions);
                    return userPermissionRepository.save(userPerms);
                })
                .map(userPermissionMapper::toDto);
    }

    public Flux<Permission> getUserPermissionsInWorkspace(UUID userId, UUID workspaceId) {
        return userPermissionRepository
                .findByUserIdAndWorkspaceId(userId, workspaceId)
                .flatMapMany(userPermission -> Flux.fromIterable(userPermission.getPermissions())
                );
    }

    //

    public Mono<Void> revokeUserPermissions(UUID principalId, UUID userId, UUID workspaceId) {
        return verifyPrincipalPermissionToGrant(principalId, workspaceId)
                .then(
                        revokeAllUserPermissionsInWorkspaceNoCheck(userId, workspaceId)
                );
    }

    public Mono<Void> revokeAllUserPermissionsInWorkspaceNoCheck(UUID userId, UUID workspaceId) {
        return userPermissionRepository
                .deleteAllByUserIdAndWorkspaceId(userId, workspaceId);
    }
}
