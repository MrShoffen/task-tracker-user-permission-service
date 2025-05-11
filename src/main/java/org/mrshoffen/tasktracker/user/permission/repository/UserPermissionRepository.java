package org.mrshoffen.tasktracker.user.permission.repository;

import org.mrshoffen.tasktracker.user.permission.model.entity.UserPermission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserPermissionRepository extends ReactiveCrudRepository<UserPermission, Long> {

    Mono<Void> deleteAllByUserIdAndWorkspaceId(UUID userId, UUID workspaceId);

    Flux<UserPermission> findAllByWorkspaceId( UUID workspaceId);

    Mono<UserPermission> findByUserIdAndWorkspaceId(UUID userId, UUID workspaceId);
}
