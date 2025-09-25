package ec.com.carrera.makers.loan.exception;

import ec.com.carrera.makers.loan.util.MessageSourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    private static final String ERROR_BAD_REQUEST_TITLE = "error.bad-request.title";

    private static final String ERROR_BAD_REQUEST_DETAIL = "error.bad-request.detail";

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ProblemDetail> handleValidationException(WebExchangeBindException ex, ServerHttpRequest request) {
        Mono<String> titleMessage = MessageSourceUtil.getMessageByCode(messageSource, ERROR_BAD_REQUEST_TITLE);
        Mono<String> detailMessage = MessageSourceUtil.getMessageByCode(messageSource, "error.validation.detail");
        return Mono.zip(detailMessage, titleMessage)
                .flatMap((tuple) -> {
                    ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
                    problem.setTitle(tuple.getT1());
                    problem.setDetail(tuple.getT2());
                    problem.setInstance(URI.create(request.getPath().toString()));
                    problem.setProperty("errors", ex.getFieldErrors()
                            .stream()
                            .map(this::formatFieldError)
                            .toList());
                    log.error("Validation error: {}", ex.getMessage(), ex);
                    return Mono.just(problem);
                });
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ProblemDetail> handleIllegalArgument(IllegalArgumentException illegalArgumentException, ServerHttpRequest request) {
        return getProblemDetail(
                "IllegalArgumentException: {}",
                ERROR_BAD_REQUEST_TITLE,
                ERROR_BAD_REQUEST_DETAIL,
                HttpStatus.BAD_REQUEST,
                illegalArgumentException,
                request);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ProblemDetail> handleServerWebInput(ServerWebInputException serverWebInputException, ServerHttpRequest request) {
        return getProblemDetail(
                "ServerWebInputException: {}",
                ERROR_BAD_REQUEST_TITLE,
                ERROR_BAD_REQUEST_DETAIL,
                HttpStatus.BAD_REQUEST,
                serverWebInputException,
                request);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public Mono<ProblemDetail> handleAuthorizationDenied(AuthorizationDeniedException authorizationDeniedException, ServerHttpRequest request) {
        return getProblemDetail(
                "AuthorizationDeniedException: {}",
                "error.forbidden.title",
                "error.forbidden.detail",
                HttpStatus.FORBIDDEN,
                authorizationDeniedException,
                request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Mono<ProblemDetail> handleNoResourceFound(NoResourceFoundException noResourceFoundException, ServerHttpRequest request) {
        String [] reason = noResourceFoundException.getReason().split("\\s+");
        String path = reason[reason.length-1];
        return getProblemDetail(
                "NoResourceFoundException: {}",
                "error.resource-not-found.title",
                "error.resource-not-found.detail",
                HttpStatus.NOT_FOUND,
                noResourceFoundException,
                request,
                path
        );
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public Mono<ProblemDetail> handleMethodNotAllowed(MethodNotAllowedException methodNotAllowedException, ServerHttpRequest request) {
        return getProblemDetail(
                "MethodNotAllowedException: {}",
                "error.method-not-allowed.title",
                "error.method-not-allowed.detail",
                HttpStatus.METHOD_NOT_ALLOWED,
                methodNotAllowedException,
                request);
    }

    @ExceptionHandler(GlobalResponseStatusException.class)
    public Mono<ProblemDetail> handleResponseStatus(GlobalResponseStatusException ex, ServerHttpRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(ex.getStatusCode());
        problem.setTitle(ex.getDetailMessageCode());
        problem.setDetail(ex.getReason());
        problem.setInstance(URI.create(request.getPath().toString()));
        log.error("GlobalResponseStatusException: {}", ex.getMessage(), ex);
        return Mono.just(problem);
    }

    @ExceptionHandler(Throwable.class)
    public Mono<ProblemDetail> handleGeneric(Throwable throwable, ServerHttpRequest request) {
        return getProblemDetail(
                "Unexpected error occurred: {}",
                "error.internal.title",
                "error.internal.detail",
                HttpStatus.INTERNAL_SERVER_ERROR,
                throwable,
                request);
    }

    private Mono<ProblemDetail> getProblemDetail(String logMessage,
                                                 String titleCode,
                                                 String detailCode,
                                                 HttpStatusCode code,
                                                 Throwable throwable,
                                                 ServerHttpRequest request,
                                                 Object... detailArgs) {
        Mono<String> titleMessage = MessageSourceUtil.getMessageByCode(messageSource, titleCode);
        Mono<String> detailMessage = MessageSourceUtil.getMessageByCode(messageSource, detailCode, detailArgs);
        return Mono.zip(titleMessage, detailMessage)
                .flatMap(tuple -> {
                    ProblemDetail problem = ProblemDetail.forStatus(code);
                    problem.setTitle(tuple.getT1());
                    problem.setDetail(tuple.getT2());
                    problem.setInstance(URI.create(request.getPath().toString()));
                    log.error(logMessage, throwable.getMessage(), throwable);
                    return Mono.just(problem);
                });
    }

    private Map<String, String> formatFieldError(FieldError error) {
        return Map.of(
                "field", error.getField(),
                "message", error.getDefaultMessage()
        );
    }
}
