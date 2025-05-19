package br.com.springstudiouslabaws.repositories;

import br.com.springstudiouslabaws.config.mongodb.MongoPaymentRepository;
import br.com.springstudiouslabaws.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.entities.PaymentsEntity;
import br.com.springstudiouslabaws.exceptions.ResourceNotFoundException;
import br.com.springstudiouslabaws.mappers.PaymentCoreToDataMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {
    
    private final MongoPaymentRepository mongoRepository;
    private final PaymentCoreToDataMapper mapper;

    public PaymentRepositoryImpl(MongoPaymentRepository mongoRepository,
                                 @Qualifier("paymentCoreToDataMapper") PaymentCoreToDataMapper mapper) {
        this.mongoRepository = mongoRepository;
        this.mapper = mapper;
    }

    @Override
    public PaymentDomain findById(String id) {
        return mongoRepository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado: " + id));
    }

    @Override
    public PaymentDomain save(PaymentDomain payment) {
        PaymentsEntity entity = mapper.toEntity(payment);
        PaymentsEntity savedEntity = mongoRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}