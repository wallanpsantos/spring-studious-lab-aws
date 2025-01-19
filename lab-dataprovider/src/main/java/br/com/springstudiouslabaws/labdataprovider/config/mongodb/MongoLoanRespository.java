package br.com.springstudiouslabaws.labdataprovider.config.mongodb;

import br.com.springstudiouslabaws.labdataprovider.entities.LoanEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoLoanRespository extends MongoRepository<LoanEntity, String> {
    // Aqui você pode adicionar consultas específicas se necessário.
}