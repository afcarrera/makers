package ec.com.carrera.makers.loan.util;

import ec.com.carrera.makers.loan.exception.GlobalResponseStatusException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class ExceptionUtil {

    public static GlobalResponseStatusException getNotFoundException(MessageSource messageSource) {
        String titleMessage = messageSource.getMessage("error.not-found.title", new Object[0], LocaleContextHolder.getLocale());
        String detailMessage = messageSource.getMessage("error.not-found.detail", new Object[0], LocaleContextHolder.getLocale());
        return new GlobalResponseStatusException(
                HttpStatus.NOT_FOUND,
                detailMessage,
                null,
                titleMessage,
                new Object[0]
        );
    }
}
