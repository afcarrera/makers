package ec.com.carrera.makers.loan.service;

import ec.com.carrera.makers.loan.entity.Loan;

public interface LoanService {

    Loan create(ec.com.carrera.makers.loan.dto.Loan domain);

    Loan update(ec.com.carrera.makers.loan.dto.Loan domain);
}
