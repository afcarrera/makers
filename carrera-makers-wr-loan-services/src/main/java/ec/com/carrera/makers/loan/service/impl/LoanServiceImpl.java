package ec.com.carrera.makers.loan.service.impl;

import ec.com.carrera.makers.loan.dto.enums.LoanStatusEnum;
import ec.com.carrera.makers.loan.entity.Loan;
import ec.com.carrera.makers.loan.handler.Handler;
import ec.com.carrera.makers.loan.mapper.LoanMapper;
import ec.com.carrera.makers.loan.repository.LoanRepository;
import ec.com.carrera.makers.loan.service.LoanService;
import ec.com.carrera.makers.loan.util.ExceptionUtil;
import ec.com.carrera.makers.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class LoanServiceImpl implements LoanService {

    SecurityService securityService;

    LoanRepository loanRepository;

    LoanMapper domainMapper;

    MessageSource messageSource;

    Handler<ec.com.carrera.makers.loan.dto.Loan> loanHandler;

    @Override
    public Loan create(ec.com.carrera.makers.loan.dto.Loan loan) {
        loan.setUserId(securityService.getUserId());
        loan.setStatusId(LoanStatusEnum.PENDIENTE.getId().toString());
        return loanRepository.save(domainMapper.mapToEntity(loan));
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Loan update(ec.com.carrera.makers.loan.dto.Loan loan) {
        Loan existingDomain = findById(loan.getId());
        loan.setPreviousStatusId(existingDomain.getStatusId());
        loanHandler.process(loan);
        domainMapper.updateEntity(loan, existingDomain);
        existingDomain.setUserId(securityService.getUserId());
        return loanRepository.save(existingDomain);
    }

    private Loan findById(String id){
        return loanRepository.findById(id)
                .orElseThrow(() -> ExceptionUtil.getNotFoundException(messageSource));
    }
}
