package ec.com.carrera.makers.loan.exception;

import feign.FeignException;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PessimisticLockException;
import jakarta.persistence.QueryTimeoutException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    private static final String ERROR_BAD_REQUEST_TITLE = "error.bad-request.title";

    private static final String ERROR_BAD_REQUEST_DETAIL = "error.bad-request.detail";

    private static final String ERROR_VALIDATION_DETAIL = "error.validation.detail";

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException, HttpServletRequest request) {
        List<Map<String, String>> errors = methodArgumentNotValidException.getFieldErrors()
                .stream()
                .map(error -> Map.of("field", error.getField(), "message", error.getDefaultMessage()))
                .toList();
        return getProblemDetail(
                "MethodArgumentNotValidException: {}",
                ERROR_BAD_REQUEST_TITLE,
                ERROR_VALIDATION_DETAIL,
                HttpStatus.BAD_REQUEST,
                new Object[0],
                new Object[0],
                request,
                methodArgumentNotValidException,
                errors
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException constraintViolationException, HttpServletRequest request){
        List<Map<String, String>> errors = constraintViolationException.getConstraintViolations()
                .stream()
                .map(error -> Map.of("field", error.getPropertyPath().toString(), "message", error.getMessage()))
                .toList();
        return getProblemDetail(
                "ConstraintViolationException: {}",
                ERROR_BAD_REQUEST_TITLE,
                ERROR_VALIDATION_DETAIL,
                HttpStatus.BAD_REQUEST,
                new Object[0],
                new Object[0],
                request,
                constraintViolationException,
                errors
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException illegalArgumentException, HttpServletRequest request) {
        return getProblemDetail(
                "IllegalArgumentException: {}",
                ERROR_BAD_REQUEST_TITLE,
                ERROR_BAD_REQUEST_DETAIL,
                HttpStatus.BAD_REQUEST,
                new Object[0],
                new Object[0],
                request,
                illegalArgumentException,
                null
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(HttpMessageNotReadableException httpMessageNotReadableException, HttpServletRequest request) {
        return getProblemDetail(
                "HttpMessageNotReadableException: {}",
                ERROR_BAD_REQUEST_TITLE,
                ERROR_BAD_REQUEST_DETAIL,
                HttpStatus.BAD_REQUEST,
                new Object[0],
                new Object[0],
                request,
                httpMessageNotReadableException,
                null
        );
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ProblemDetail handleAuthorizationDenied(AuthorizationDeniedException authorizationDeniedException, HttpServletRequest request) {
        return getProblemDetail(
                "AuthorizationDeniedException: {}",
                "error.forbidden.title",
                "error.forbidden.detail",
                HttpStatus.FORBIDDEN,
                new Object[0],
                new Object[0],
                request,
                authorizationDeniedException,
                null
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException, HttpServletRequest request) {
        return getProblemDetail(
                "HttpRequestMethodNotSupportedException: {}",
                "error.method-not-allowed.title",
                "error.method-not-allowed.detail",
                HttpStatus.METHOD_NOT_ALLOWED,
                new Object[0],
                new Object[0],
                request,
                httpRequestMethodNotSupportedException,
                null
        );
    }

    @ExceptionHandler(GlobalResponseStatusException.class)
    public ProblemDetail handleResponseStatus(GlobalResponseStatusException globalResponseStatusException, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(globalResponseStatusException.getStatusCode());
        problemDetail.setTitle(globalResponseStatusException.getDetailMessageCode());
        problemDetail.setDetail(globalResponseStatusException.getReason());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        if (globalResponseStatusException.getErrors() != null) {
            problemDetail.setProperty("errors", globalResponseStatusException.getErrors());
        }
        log.error("GlobalResponseStatusException: {}", globalResponseStatusException.getMessage(), globalResponseStatusException);
        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException dataIntegrityViolationException, HttpServletRequest request) {
        String rootMessage = Optional.of(dataIntegrityViolationException.getRootCause())
                .map(Throwable::getMessage)
                .orElse(dataIntegrityViolationException.getMessage());
        Object[] arguments = new Object[1];
        arguments[0] = extractFieldFromMySQLMessage(rootMessage);
        return getProblemDetail(
                "DataIntegrityViolationException: {}",
                "error.conflict.integrity.title",
                "error.conflict.integrity.detail",
                HttpStatus.CONFLICT,
                new Object[0],
                arguments,
                request,
                dataIntegrityViolationException,
                null
        );
    }

    @ExceptionHandler({ OptimisticLockException.class, ObjectOptimisticLockingFailureException.class })
    public ProblemDetail handleOptimisticLock(Throwable throwable, HttpServletRequest request) {
        return getProblemDetail(
                "OptimisticLockException: {}",
                "error.conflict.optimistic.title",
                "error.conflict.optimistic.detail",
                HttpStatus.CONFLICT,
                new Object[0],
                new Object[0],
                request,
                throwable,
                null
        );
    }

    @ExceptionHandler({ PessimisticLockException.class, LockTimeoutException.class, QueryTimeoutException.class })
    public ProblemDetail handlePessimisticLock(Throwable throwable, HttpServletRequest request) {
        return getProblemDetail(
                "PessimisticLockException: {}",
                "error.locked.title",
                "error.locked.detail",
                HttpStatus.LOCKED,
                new Object[0],
                new Object[0],
                request,
                throwable,
                null
        );
    }

    @ExceptionHandler(FeignException.class)
    public ProblemDetail handleFeignException(FeignException feignException, HttpServletRequest request) {
        String title = messageSource.getMessage("error.feign.title", new Object[0], LocaleContextHolder.getLocale());

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(feignException.status()==-1?HttpStatus.INTERNAL_SERVER_ERROR.value():feignException.status()));
        problemDetail.setTitle(title);
        problemDetail.setDetail(feignException.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        log.error("FeignException: {}", feignException.getMessage(), feignException);
        return problemDetail;
    }

    @ExceptionHandler(Throwable.class)
    public ProblemDetail handleGeneric(Throwable throwable, HttpServletRequest request) {
        return getProblemDetail(
                "Unexpected error occurred: {}",
                "error.internal.title",
                "error.internal.detail",
                HttpStatus.INTERNAL_SERVER_ERROR,
                new Object[0],
                new Object[0],
                request,
                throwable,
                null
        );
    }

    private ProblemDetail getProblemDetail(String logMessage,
                                           String titleCode,
                                           String detailCode,
                                           HttpStatusCode code,
                                           Object[] titleArguments,
                                           Object[] detailArguments,
                                           HttpServletRequest request,
                                           Throwable throwable,
                                           List<Map<String, String>> errors) {
        String title = messageSource.getMessage(titleCode, titleArguments, LocaleContextHolder.getLocale());
        String detail = messageSource.getMessage(detailCode, detailArguments, LocaleContextHolder.getLocale());

        ProblemDetail problemDetail = ProblemDetail.forStatus(code);
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        if (errors != null) {
            problemDetail.setProperty("errors", errors);
        }

        log.error(logMessage, throwable.getMessage(), throwable);
        return problemDetail;
    }

    private String extractFieldFromMySQLMessage(String message) {
        List<String> patterns = List.of(
                "for key '(.+?)'",
                "FOREIGN KEY \\(`(\\w+)`\\)",
                "Data too long for column '(\\w+)'"
        );

        return patterns.stream()
                .map(p -> extractFieldFromMySQLMessage(message, p))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse("{no available}");
    }

    private String extractFieldFromMySQLMessage(String message, String regex) {
        if (message != null) {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(message);

            if (matcher.find()) {
                String key = matcher.group(1);
                return transformToCamelCase(key);
            }
        }
        return null;
    }

    private String transformToCamelCase(String key) {
        String[] parts = key.split("_");
        StringBuilder camelCase = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            camelCase.append(parts[i].substring(0, 1).toUpperCase())
                    .append(parts[i].substring(1));
        }
        String[] dotParts = camelCase.toString().split("\\.");
        return dotParts[dotParts.length - 1];
    }
}
