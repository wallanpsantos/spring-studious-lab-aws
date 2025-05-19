package br.com.springstudiouslabaws.config.mongodb;

import br.com.springstudiouslabaws.entities.PaymentsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoPaymentRepository extends MongoRepository<PaymentsEntity, String> {
    // Aqui você pode adicionar consultas específicas se necessário.
}