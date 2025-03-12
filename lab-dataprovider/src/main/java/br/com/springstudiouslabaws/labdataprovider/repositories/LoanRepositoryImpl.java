package br.com.springstudiouslabaws.labdataprovider.repositories;

import br.com.springstudiouslabaws.labcore.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.labcore.repositories.LoanRepository;
import br.com.springstudiouslabaws.labdataprovider.config.mongodb.MongoLoanRespository;
import br.com.springstudiouslabaws.labdataprovider.entities.LoanEntity;
import br.com.springstudiouslabaws.labdataprovider.mappers.LoanCoreToDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class LoanRepositoryImpl implements LoanRepository {

    private static final Logger log = LoggerFactory.getLogger(LoanRepositoryImpl.class);

    private final MongoLoanRespository mongoRepository;
    private final LoanCoreToDataMapper mapper;

    public LoanRepositoryImpl(@Qualifier("loanCoreToDataMapperImpl") LoanCoreToDataMapper mapper, MongoLoanRespository mongoRepository) {
        this.mapper = mapper;
        this.mongoRepository = mongoRepository;
    }

    @Override
    public LoanDomain save(LoanDomain loan) {
        log.debug("Salvando empréstimo com ID: {}", loan.getLoanId());
        LoanEntity entity = mongoRepository.save(mapper.toEntity(loan));
        return mapper.toDomain(entity);
    }

    @Override
    public boolean existsLoanId(String loanId) {
        log.debug("Verificando existência do empréstimo com ID: {}", loanId);
        return mongoRepository.existsByLoanId(loanId);
    }

    @Override
    public boolean existsClientId(String clientId) {
        log.debug("Verificando existência de empréstimos para o cliente com ID: {}", clientId);
        return mongoRepository.existsByClientClientId(clientId);
    }

    @Override
    public LoanDomain findLoanByLoanId(String loanId) {
        log.debug("Buscar dados de empréstimo com ID: {}", loanId);
        LoanEntity entity = mongoRepository.findLoanEntityByLoanId(loanId);
        return mapper.toDomain(entity);
    }

}
