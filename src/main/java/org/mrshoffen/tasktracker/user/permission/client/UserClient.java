package org.mrshoffen.tasktracker.user.permission.client;


import lombok.RequiredArgsConstructor;
import org.mrshoffen.tasktracker.commons.web.exception.EntityNotFoundException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class UserClient {

    private final WebClient webClient;

    public Mono<UUID> getUserId(String email) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/internal/users/id")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(UUID::fromString)
                .onErrorMap(ex -> ex instanceof WebClientResponseException.NotFound,
                        ex -> new EntityNotFoundException("Пользователь с данным email '%s' не найден"
                                .formatted(email)));
    }
}
