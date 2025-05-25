package org.mrshoffen.tasktracker.user.permission.model.dto.links;

import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.commons.utils.link.Link;
import org.mrshoffen.tasktracker.commons.utils.link.Links;
import org.mrshoffen.tasktracker.commons.utils.link.LinksInjector;
import org.mrshoffen.tasktracker.commons.web.dto.UserPermissionResponseDto;

@RequiredArgsConstructor
public class UserPermissionResponseDtoLinksInjector extends LinksInjector<UserPermissionResponseDto> {

    private final String apiPrefix;

    @Override
    protected Links generateLinks(UserPermissionResponseDto dto) {
        return Links.builder()
                .addLink(Link.forName("allPermissions")
                        .andHref(apiPrefix + "/workspaces/" + dto.getWorkspaceId() + "/permissions")
                        .andMethod("GET")
                        .build()
                )
                .addLink(Link.forName("createPermission")
                        .andHref(apiPrefix + "/workspaces/" + dto.getWorkspaceId() + "/permissions/by-email")
                        .andMethod("POST")
                        .build()
                )
                .addLink(Link.forName("revokePermissions")
                        .andHref(apiPrefix + "/workspaces/" + dto.getWorkspaceId() + "/permissions/" + dto.getUserId())
                        .andMethod("DELETE")
                        .build()
                )
                .build();
    }
}
