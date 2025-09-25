package ec.com.carrera.makers.security.exception;

import java.io.Serial;

public class SecurityException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1159200000L;

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
