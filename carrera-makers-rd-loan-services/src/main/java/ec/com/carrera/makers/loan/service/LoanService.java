package ec.com.carrera.makers.loan.service;

import ec.com.carrera.makers.loan.entity.Loan;
import reactor.core.publisher.Mono;

import java.util.List;

public interface LoanService {
    Mono<List<Loan>> getLoans();

    Mono<Loan> getLoanById(String id);
}