package ec.com.carrera.makers.loan.config;

import ec.com.carrera.makers.loan.dto.Loan;
import ec.com.carrera.makers.loan.handler.Handler;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class LoanConfig {

    Handler<Loan> statusHandler;

    @Bean
    public Handler<Loan> loanHandler() {
        return statusHandler;
    }
}
