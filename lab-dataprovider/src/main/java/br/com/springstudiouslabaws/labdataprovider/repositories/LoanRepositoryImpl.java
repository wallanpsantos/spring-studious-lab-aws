package br.com.springstudiouslabaws.labdataprovider.repositories;

import br.com.springstudiouslabaws.labcore.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.labcore.repositories.LoanRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LoanRepositoryImpl implements LoanRepository {

    @Override
    public LoanDomain save(LoanDomain loan) {
        return null;
    }
}
