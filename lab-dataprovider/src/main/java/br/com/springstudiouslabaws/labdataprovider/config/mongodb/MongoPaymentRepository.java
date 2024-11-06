package br.com.springstudiouslabaws.labdataprovider.config.mongodb;

import br.com.springstudiouslabaws.labdataprovider.entities.PaymentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoPaymentRepository extends MongoRepository<PaymentEntity, String> {
    // Aqui você pode adicionar consultas específicas se necessário.
}