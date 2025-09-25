package ec.com.carrera.makers.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class OAuth2ExceptionHandler implements ServerAuthenticationEntryPoint, ServerAccessDeniedHandler {


    private final ObjectMapper securityObjectMapper;

    MediaType PROBLEM_JSON = MediaType.parseMediaType("application/problem+json");

    public OAuth2ExceptionHandler(ObjectMapper securityObjectMapper) {
        this.securityObjectMapper = securityObjectMapper;
    }

    @Override
    public Mono<Void> commence(ServerWebExchange exchange,
                               AuthenticationException authException) {
        return setBodyResponse(
                            "No authorized",
                            "Invalid or expired token",
                            HttpStatus.UNAUTHORIZED, exchange);
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange,
                             AccessDeniedException accessDeniedException) {
        return setBodyResponse(
                            "Access denied",
                            "You do not have permission to access this resource",
                            HttpStatus.FORBIDDEN, exchange);
    }

    private Mono<Void> setBodyResponse(String title, String detail, HttpStatus status, ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(PROBLEM_JSON);

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);
        problem.setInstance(URI.create(exchange.getRequest().getPath().toString()));
        byte[] responseBody;
        try {
            responseBody = securityObjectMapper.writeValueAsBytes(problem);
        } catch (Exception e) {
            responseBody = new byte[0];
        }

        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                .bufferFactory().wrap(responseBody)));
    }
}
