package org.mrshoffen.tasktracker.user.permission.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mrshoffen.tasktracker.commons.web.permissions.Permission;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.annotation.Id;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table("users_permissions")
public class UserPermission {

    @Id
    @Column("id")
    private Long id;

    @Column("user_id")
    private UUID userId;

    @Column("workspace_id")
    private UUID workspaceId;

    @Column("permissions")
    private List<Permission> permissions;


    @WritingConverter
    public static class PermissionListToStringConverter implements Converter<List<Permission>, String> {
        @Override
        public String convert(List<Permission> source) {
            return source.stream()
                    .map(Enum::name)
                    .collect(Collectors.joining(","));
        }
    }

    @ReadingConverter
    public static class StringToPermissionListConverter implements Converter<String, List<Permission>> {
        @Override
        public List<Permission> convert(String source) {
            return Arrays.stream(source.split(","))
                    .map(Permission::valueOf)
                    .toList();
        }
    }

}
