package ec.com.carrera.makers.security.service.impl;

import ec.com.carrera.makers.security.service.SecurityService;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SecurityServiceImpl implements SecurityService {
    private final ReactiveOAuth2AuthorizedClientManager authorizedClientManager;

    public SecurityServiceImpl(ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        this.authorizedClientManager = authorizedClientManager;
    }

    public Mono<String> getAccessToken() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                            .withClientRegistrationId("sso-client")
                            .principal(authentication)
                            .build();
                    return authorizedClientManager.authorize(authorizeRequest)
                                        .map(OAuth2AuthorizedClient::getAccessToken)
                                        .map(token -> token != null ? token.getTokenValue() : null)
                                        .switchIfEmpty(Mono.error(new SecurityException(
                                                "Cannot get access token from authorized client"
                                        )));
                });
    }

    public Mono<String> getUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> {
                    Jwt jwt = (Jwt) ctx.getAuthentication().getPrincipal();
                    return jwt.getClaimAsString("sub");
                });
    }
}
