package ec.com.carrera.makers.loan.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class GlobalResponseStatusException extends ResponseStatusException {

    public GlobalResponseStatusException(HttpStatusCode status, String reason) {
        super(status, reason);
    }

    public GlobalResponseStatusException(HttpStatusCode status, String reason, Throwable cause, String messageDetailCode, Object[] messageDetailArguments) {
        super(status, reason, cause, messageDetailCode, messageDetailArguments);
    }
}
