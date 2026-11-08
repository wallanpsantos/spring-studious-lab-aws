package br.com.springstudiouslabaws.repositories;

import br.com.springstudiouslabaws.domain.loan.LoanDomain;

import java.math.BigDecimal;

public interface LoanRepository {

    LoanDomain save(LoanDomain loan);

    void applyDebit(String loanId, BigDecimal amount);

    boolean existsLoanId(String loanId);

    boolean existsClientId(String clientId);

    LoanDomain findLoanByLoanId(String loanId);
}
