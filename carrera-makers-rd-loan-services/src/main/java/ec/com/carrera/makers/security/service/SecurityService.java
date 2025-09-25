package ec.com.carrera.makers.security.service;

import reactor.core.publisher.Mono;

public interface SecurityService {

    Mono<String> getAccessToken();

    Mono<String> getUserId();
}

