package ec.com.carrera.makers.loan.handler.impl;

import ec.com.carrera.makers.loan.dto.Loan;
import ec.com.carrera.makers.loan.dto.enums.LoanStatusEnum;
import ec.com.carrera.makers.loan.exception.GlobalResponseStatusException;
import ec.com.carrera.makers.loan.handler.AbstractHandler;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class StatusHandler extends AbstractHandler<Loan> {
    @Override
    public void process(Loan context) {
        LoanStatusEnum status = LoanStatusEnum.fromId(UUID.fromString(context.getStatusId())).orElseThrow(
                () -> new GlobalResponseStatusException(HttpStatus.CONFLICT, "Invalid status ID")
        );
        switch (status) {
            case PENDIENTE -> {
                if (context.getPreviousStatusId() != null) {
                    throw new GlobalResponseStatusException(HttpStatus.CONFLICT,
                            "Cannot change status from " + context.getPreviousStatusId() + " to PENDIENTE");
                }
            }
            case APROBADO -> {
                if (context.getPreviousStatusId() == null || !context.getPreviousStatusId().equals(LoanStatusEnum.PENDIENTE.getId().toString())) {
                    throw new GlobalResponseStatusException(HttpStatus.CONFLICT,
                            "Cannot change status from " + context.getPreviousStatusId() + " to APROBADO");
                }
            }
            case RECHAZADO -> {
                if (context.getPreviousStatusId() == null || !context.getPreviousStatusId().equals(LoanStatusEnum.PENDIENTE.getId().toString())) {
                    throw new GlobalResponseStatusException(HttpStatus.CONFLICT,
                            "Cannot change status from " + context.getPreviousStatusId() + " to RECHAZADO");
                }
            }
            default -> throw new GlobalResponseStatusException(HttpStatus.CONFLICT, "Invalid status");
        }
        checkNextHandler(context);
    }
}
