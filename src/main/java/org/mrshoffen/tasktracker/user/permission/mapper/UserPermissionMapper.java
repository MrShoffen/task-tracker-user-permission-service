package org.mrshoffen.tasktracker.user.permission.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mrshoffen.tasktracker.commons.web.dto.UserPermissionResponseDto;
import org.mrshoffen.tasktracker.commons.web.permissions.Permission;
import org.mrshoffen.tasktracker.user.permission.model.entity.UserPermission;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserPermissionMapper {

    UserPermission toEntity(UUID userId, UUID workspaceId, List<Permission> permissions);

    UserPermissionResponseDto toDto(UserPermission userPermission);
}
