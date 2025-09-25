package ec.com.carrera.makers.loan.service;

import ec.com.carrera.makers.loan.entity.Loan;
import ec.com.carrera.makers.loan.entity.LoanStatus;
import reactor.core.publisher.Mono;

import java.util.List;

public interface LoanStatusService {

    Mono<LoanStatus> getLoanStatusById(String id);
}