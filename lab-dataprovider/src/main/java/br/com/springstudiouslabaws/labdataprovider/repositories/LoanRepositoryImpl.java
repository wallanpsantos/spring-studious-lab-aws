package br.com.springstudiouslabaws.labdataprovider.repositories;

import br.com.springstudiouslabaws.labcore.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.labcore.repositories.LoanRepository;
import br.com.springstudiouslabaws.labdataprovider.config.mongodb.MongoLoanRespository;
import br.com.springstudiouslabaws.labdataprovider.entities.LoanEntity;
import br.com.springstudiouslabaws.labdataprovider.mappers.LoanCoreToDataMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class LoanRepositoryImpl implements LoanRepository {

    private final MongoLoanRespository mongoRepository;
    private final LoanCoreToDataMapper mapper;

    public LoanRepositoryImpl(@Qualifier("loanCoreToDataMapperImpl") LoanCoreToDataMapper mapper, MongoLoanRespository mongoRepository) {
        this.mapper = mapper;
        this.mongoRepository = mongoRepository;
    }

    @Override
    public LoanDomain save(LoanDomain loan) {
        LoanEntity entity = mongoRepository.save(mapper.toEntity(loan));
        return mapper.toDomain(entity);
    }

}
