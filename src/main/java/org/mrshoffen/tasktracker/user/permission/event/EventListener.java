package org.mrshoffen.tasktracker.user.permission.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mrshoffen.tasktracker.commons.kafka.event.workspace.WorkspaceCreatedEvent;
import org.mrshoffen.tasktracker.commons.kafka.event.workspace.WorkspaceDeletedEvent;
import org.mrshoffen.tasktracker.user.permission.service.PermissionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventListener {

    private final PermissionService permissionService;

    @KafkaListener(topics = WorkspaceCreatedEvent.TOPIC)
    public void handleNewWorkspaceCreation(WorkspaceCreatedEvent event) {
        log.info("Received event in topic {} - {}", WorkspaceCreatedEvent.TOPIC, event);
        permissionService
                .grantAllPermissionsToWorkspaceOwner(event.getCreatedWorkspace().getUserId(), event.getCreatedWorkspace().getId())
                .block();
    }

    @KafkaListener(topics = WorkspaceDeletedEvent.TOPIC)
    public void handleSuccessfulRegistration(WorkspaceDeletedEvent event) {
        log.info("Received event in topic {} - {}", WorkspaceDeletedEvent.TOPIC, event);
        permissionService
                .revokeAllUserPermissionsInWorkspaceNoCheck(event.getUserId(), event.getWorkspaceId())
                .block();
    }

}
