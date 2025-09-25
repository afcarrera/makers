package ec.com.carrera.makers.loan.util;

import reactor.core.publisher.Mono;

import java.util.Locale;

public class LocaleContextUtil {

    private LocaleContextUtil() {
    }

    public static Mono<Locale> currentLocale() {
        return Mono.deferContextual(ctx -> {
            if (ctx.hasKey(Locale.class)) {
                return Mono.just(ctx.get(Locale.class));
            } else {
                return Mono.just(Locale.getDefault());
            }
        });
    }
}
