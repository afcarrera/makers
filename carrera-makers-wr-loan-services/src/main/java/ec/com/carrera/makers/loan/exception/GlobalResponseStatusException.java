package ec.com.carrera.makers.loan.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

public class GlobalResponseStatusException extends ResponseStatusException {

    private List<Map<String, String>> errors;

    public GlobalResponseStatusException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public GlobalResponseStatusException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }

    public GlobalResponseStatusException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments, List<Map<String, String>> errors) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
        this.errors = errors;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }
}
