package org.mrshoffen.tasktracker.user.permission;

import io.r2dbc.spi.ConnectionFactory;
import org.mrshoffen.tasktracker.user.permission.client.UserClient;
import org.mrshoffen.tasktracker.user.permission.model.dto.links.UserPermissionResponseDtoLinksInjector;
import org.mrshoffen.tasktracker.user.permission.model.entity.UserPermission;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class PermissionServiceBeans {

    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions(ConnectionFactory connectionFactory) {
        var dialect = DialectResolver.getDialect(connectionFactory);
        var converters = List.of(
                new UserPermission.PermissionListToStringConverter(),
                new UserPermission.StringToPermissionListConverter()
        );
        return R2dbcCustomConversions.of(dialect, converters);
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public UserClient userClient(WebClient.Builder webClientBuilder) {
        return new UserClient(webClientBuilder.baseUrl("http://user-profile-ws").build());
    }

    @Bean
    UserPermissionResponseDtoLinksInjector UserPermissionResponseDtoLinksInjector(@Value("${app.gateway.api-prefix}") String apiPrefix) {
        return new UserPermissionResponseDtoLinksInjector(apiPrefix);
    }


}
