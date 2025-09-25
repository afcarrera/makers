package ec.com.carrera.makers.loan.util;

import org.springframework.context.MessageSource;
import reactor.core.publisher.Mono;

public class MessageSourceUtil {
    private MessageSourceUtil() {
    }

    public static Mono<String> getMessageByCode(MessageSource messageSource, String code, Object... args) {
        return LocaleContextUtil.currentLocale()
                .map(locale -> messageSource.getMessage(code, args, locale));
    }
}
