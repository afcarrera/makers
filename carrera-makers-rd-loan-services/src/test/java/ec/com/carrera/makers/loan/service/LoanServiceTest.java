package ec.com.carrera.makers.loan.service;

import ec.com.carrera.makers.loan.entity.Loan;
import ec.com.carrera.makers.loan.entity.LoanStatus;
import ec.com.carrera.makers.loan.repository.LoanRepository;
import ec.com.carrera.makers.loan.service.impl.LoanServiceImpl;
import ec.com.carrera.makers.security.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    MessageSource messageSource;

    @Mock
    SecurityService securityService;

    @Mock
    LoanRepository loanRepository;
    @Mock
    LoanStatusService loanStatusService;

    @InjectMocks
    LoanServiceImpl loanService;

    @Test
    void testGetAll() {

        Loan loan1 = new Loan();
        loan1.setId("1L");
        loan1.setAmount(new BigDecimal("1000"));
        loan1.setTerm(12);
        loan1.setStatusId("10L");

        Loan loan2 = new Loan();
        loan2.setId("2L");
        loan2.setAmount(new BigDecimal("2000"));
        loan2.setTerm(24);
        loan2.setStatusId("20L");

        LoanStatus status1 = new LoanStatus();
        status1.setId("10L");
        status1.setName("APPROVED");

        LoanStatus status2 = new LoanStatus();
        status2.setId("20L");
        status2.setName("PENDING");

        // Mocks
        when(securityService.getUserId()).thenReturn(Mono.just("userId"));
        when(loanRepository.findByUserId("userId")).thenReturn(Flux.just(loan1, loan2));
        when(loanStatusService.getLoanStatusById("10L")).thenReturn(Mono.just(status1));
        when(loanStatusService.getLoanStatusById("20L")).thenReturn(Mono.just(status2));

        StepVerifier.create(loanService.getLoans())
                .expectNextMatches(loans -> {
                    if (loans.size() != 2) return false;

                    return loans.get(0).getStatus() == status1
                            && loans.get(1).getStatus() == status2;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void testGetLoanById() {
        Loan loan = new Loan();

        when(messageSource.getMessage(anyString(), any(Object[].class), any(Locale.class))).thenReturn("error");
        when(loanRepository.findByIdAndUserId(anyString(), anyString())).thenReturn(Mono.just(loan));
        when(securityService.getUserId()).thenReturn(Mono.just("userId"));
        StepVerifier.create(loanService.getLoanById("id"))
                .expectNextMatches(result -> result.getId() != null)
                .verifyComplete();
    }

}
