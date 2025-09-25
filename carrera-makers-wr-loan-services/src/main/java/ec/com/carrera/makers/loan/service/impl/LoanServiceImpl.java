package ec.com.carrera.makers.loan.service.impl;

import ec.com.carrera.makers.loan.entity.Loan;
import ec.com.carrera.makers.loan.mapper.LoanMapper;
import ec.com.carrera.makers.loan.repository.LoanRepository;
import ec.com.carrera.makers.loan.service.LoanService;
import ec.com.carrera.makers.loan.util.ExceptionUtil;
import ec.com.carrera.makers.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class LoanServiceImpl implements LoanService {

    SecurityService securityService;

    LoanRepository loanRepository;

    LoanMapper domainMapper;

    MessageSource messageSource;

    @Override
    public Loan create(ec.com.carrera.makers.loan.dto.Loan loan) {
        loan.setUserId(securityService.getUserId());
        return loanRepository.save(domainMapper.mapToEntity(loan));
    }

    @Override
    public Loan update(ec.com.carrera.makers.loan.dto.Loan domain) {
        Loan existingDomain = findById(domain.getId());
        domain.setPreviousStatusId(existingDomain.getStatus().getId());
        domainMapper.updateEntity(domain, existingDomain);
        return loanRepository.save(existingDomain);
    }


    private Loan findById(String id){
        return loanRepository.findById(id)
                .orElseThrow(() -> ExceptionUtil.getNotFoundException(messageSource));
    }
}
