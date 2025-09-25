package ec.com.carrera.makers.loan.service.impl;

import ec.com.carrera.makers.loan.entity.LoanStatus;
import ec.com.carrera.makers.loan.exception.GlobalResponseStatusException;
import ec.com.carrera.makers.loan.repository.LoanStatusRepository;
import ec.com.carrera.makers.loan.service.LoanStatusService;
import ec.com.carrera.makers.loan.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class LoanStatusServiceImpl implements LoanStatusService {

    LoanStatusRepository loanRepository;

    MessageSource messageSource;

    @Override
    public Mono<LoanStatus> getLoanStatusById(String id) {
        Mono<String> errorTitle = MessageSourceUtil.getMessageByCode(messageSource, "error.not-found.title");
        Mono<String> errorDetail = MessageSourceUtil.getMessageByCode(messageSource, "error.not-found.detail");
        return Mono.zip(errorTitle, errorDetail)
                .flatMap(tuple -> {
                            String title = tuple.getT1();
                            String detail = tuple.getT2();
                            return loanRepository.findById(id)
                                    .switchIfEmpty(Mono.error(new GlobalResponseStatusException(
                                            org.springframework.http.HttpStatus.NOT_FOUND,
                                            detail,
                                            null,
                                            title,
                                            new Object[0]
                                    )));
                });
    }

}
