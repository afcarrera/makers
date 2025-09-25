package ec.com.carrera.makers.loan.repository;

import ec.com.carrera.makers.loan.entity.Loan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends CrudRepository<Loan, String> {
    Optional<Loan> findByIdAndUserId(String id, String userId);
}
