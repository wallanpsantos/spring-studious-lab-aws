package br.com.springstudiouslabaws.repositories;

import br.com.springstudiouslabaws.domain.loan.LoanDomain;

public interface LoanRepository {

    LoanDomain save(LoanDomain loan);

    boolean existsLoanId(String loanId);

    boolean existsClientId(String clientId);

    LoanDomain findLoanByLoanId(String loanId);
}
