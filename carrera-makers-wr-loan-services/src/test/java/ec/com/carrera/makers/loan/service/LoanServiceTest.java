package ec.com.carrera.makers.loan.service;

import ec.com.carrera.makers.loan.entity.Loan;
import ec.com.carrera.makers.loan.entity.LoanStatus;
import ec.com.carrera.makers.loan.handler.Handler;
import ec.com.carrera.makers.loan.mapper.LoanMapper;
import ec.com.carrera.makers.loan.repository.LoanRepository;
import ec.com.carrera.makers.loan.service.impl.LoanServiceImpl;
import ec.com.carrera.makers.security.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @InjectMocks
    LoanServiceImpl loanService;

    @Mock
    SecurityService securityService;

    @Mock
    LoanRepository loanRepository;

    @Mock
    LoanMapper loanMapper;

    @Mock
    MessageSource messageSource;

    @Mock
    Handler<ec.com.carrera.makers.loan.dto.Loan> loanHandler;

    @Test
    void testCreate() {
        when(securityService.getUserId()).thenReturn("userId");
        when(loanRepository.save(any())).thenReturn(new Loan());
        when(loanMapper.mapToEntity(any(ec.com.carrera.makers.loan.dto.Loan.class))).thenReturn(new Loan());

        Loan domain = loanService.create(new ec.com.carrera.makers.loan.dto.Loan());

        assertNotNull(domain);
    }

    @Test
    void testUpdate() {
        Loan existingLoan = new Loan();
        existingLoan.setStatusId("11111111-1111-1111-1111-111111111111");
        when(loanRepository.findById(any())).thenReturn(java.util.Optional.of(existingLoan));
        when(loanRepository.save(any())).thenReturn(new Loan());
        when(securityService.getUserId()).thenReturn("userId");
        doNothing().when(loanHandler).process(any());
        Loan domain = loanService.update(new ec.com.carrera.makers.loan.dto.Loan());

        assertNotNull(domain);
    }

}
