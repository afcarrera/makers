package ec.com.carrera.makers.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
public class OAuth2ExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final MessageSource messageSource;

    private final ObjectMapper securityObjectMapper;

    private static final String PROBLEM_JSON = "application/problem+json";

    public OAuth2ExceptionHandler(MessageSource messageSource, ObjectMapper securityObjectMapper) {
        this.messageSource = messageSource;
        this.securityObjectMapper = securityObjectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        setBodyResponse(
                messageSource.getMessage("error.unauthorized.message", null, LocaleContextHolder.getLocale()),
                messageSource.getMessage("error.unauthorized.detail", null, LocaleContextHolder.getLocale()),
                HttpServletResponse.SC_UNAUTHORIZED, request, response);
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        setBodyResponse(
                messageSource.getMessage("error.forbidden.message", null, LocaleContextHolder.getLocale()),
                messageSource.getMessage("error.forbidden.detail", null, LocaleContextHolder.getLocale()),
                HttpServletResponse.SC_FORBIDDEN, request, response);
    }

    private void setBodyResponse(String title, String detail, int status, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setStatus(status);
        response.setContentType(PROBLEM_JSON);

        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        response.getOutputStream().write(securityObjectMapper.writeValueAsBytes(problemDetail));
    }
}
