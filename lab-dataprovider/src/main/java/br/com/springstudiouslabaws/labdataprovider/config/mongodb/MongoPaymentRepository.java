package br.com.springstudiouslabaws.labdataprovider.config.mongodb;

import br.com.springstudiouslabaws.labdataprovider.entities.PaymentsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoPaymentRepository extends MongoRepository<PaymentsEntity, String> {
    // Aqui você pode adicionar consultas específicas se necessário.
}