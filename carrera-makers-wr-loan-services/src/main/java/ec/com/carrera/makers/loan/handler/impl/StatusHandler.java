package ec.com.carrera.makers.loan.handler.impl;

import ec.com.carrera.makers.loan.dto.Loan;
import ec.com.carrera.makers.loan.handler.AbstractHandler;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class StatusHandler extends AbstractHandler<Loan> {
    @Override
    public void process(Loan context) {
        // Implement status handling logic here
        checkNextHandler(context);
    }
}
