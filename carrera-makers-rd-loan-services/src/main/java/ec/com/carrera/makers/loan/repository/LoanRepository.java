package ec.com.carrera.makers.loan.repository;

import ec.com.carrera.makers.loan.entity.Loan;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LoanRepository extends ReactiveSortingRepository<Loan, String> {
    Mono<Loan> findByIdAndUserId(String id, String userId);

    Flux<Loan> findByUserId(String userId);
}
