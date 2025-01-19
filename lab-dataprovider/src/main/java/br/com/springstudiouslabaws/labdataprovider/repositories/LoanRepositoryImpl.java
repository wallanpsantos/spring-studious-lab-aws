package br.com.springstudiouslabaws.labdataprovider.repositories;

import br.com.springstudiouslabaws.labcore.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.labcore.repositories.LoanRepository;
import br.com.springstudiouslabaws.labdataprovider.config.mongodb.MongoLoanRespository;
import org.springframework.stereotype.Repository;

@Repository
public class LoanRepositoryImpl implements LoanRepository {

    private final MongoLoanRespository mongoRepository;

    public LoanRepositoryImpl(MongoLoanRespository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public LoanDomain save(LoanDomain loan) {
        return null;
    }
}
