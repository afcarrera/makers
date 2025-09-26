package ec.com.carrera.makers.loan.service.impl;

import ec.com.carrera.makers.loan.entity.Loan;
import ec.com.carrera.makers.loan.exception.GlobalResponseStatusException;
import ec.com.carrera.makers.loan.repository.LoanRepository;
import ec.com.carrera.makers.loan.service.LoanService;
import ec.com.carrera.makers.loan.service.LoanStatusService;
import ec.com.carrera.makers.loan.util.MessageSourceUtil;
import ec.com.carrera.makers.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class LoanServiceImpl implements LoanService {

    SecurityService securityService;

    LoanRepository loanRepository;

    LoanStatusService loanStatusService;

    MessageSource messageSource;

    @Override
    public Mono<List<Loan>> getLoans() {
        return securityService.getUserId()
                .flatMap(userId ->
                        loanRepository.findByUserId(userId)
                                .flatMap(loan ->
                                        loanStatusService.getLoanStatusById(loan.getStatusId())
                                                .map(loanStatus -> {
                                                    loan.setStatus(loanStatus);
                                                    return loan;
                                                })
                                )
                                .collectList()
                );
    }

    @Override
    public Mono<Loan> getLoanById(String id) {
        Mono<String> errorTitle = MessageSourceUtil.getMessageByCode(messageSource, "error.not-found.title");
        Mono<String> errorDetail = MessageSourceUtil.getMessageByCode(messageSource, "error.not-found.detail");
        Mono<String> userIdMono = securityService.getUserId();
        return Mono.zip(errorTitle, errorDetail, userIdMono)
                .flatMap(tuple -> {
                            String title = tuple.getT1();
                            String detail = tuple.getT2();
                            String userId = tuple.getT3();
                            return loanRepository.findByIdAndUserId(id, userId)
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
