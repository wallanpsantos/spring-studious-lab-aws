package br.com.springstudiouslabaws.labcore.repositories;

import br.com.springstudiouslabaws.labcore.domain.loan.LoanDomain;

public interface LoanRepository {

    LoanDomain save(LoanDomain loan);
}
