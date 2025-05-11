package org.mrshoffen.tasktracker.user.permission;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.user.permission.model.entity.UserPermission;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.DialectResolver;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class TaskTrackerUserPermissionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerUserPermissionServiceApplication.class, args);
    }

}
