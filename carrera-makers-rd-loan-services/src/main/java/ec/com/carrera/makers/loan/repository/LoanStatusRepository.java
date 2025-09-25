package ec.com.carrera.makers.loan.repository;

import ec.com.carrera.makers.loan.entity.LoanStatus;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface LoanStatusRepository extends ReactiveSortingRepository<LoanStatus, String> {
    Mono<LoanStatus> findById(String id);
}
