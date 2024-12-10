package br.com.springstudiouslabaws.labdataprovider.repositories;

import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.labcore.repositories.PaymentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

//    private final MongoPaymentRepository mongoRepository;
//    private final CoreToDataMapper mapper;

//    public PaymentRepositoryImpl(MongoPaymentRepository mongoRepository, CoreToDataMapper mapper) {
//        this.mongoRepository = mongoRepository;
//        this.mapper = mapper;
//    }

    @Override
    public PaymentDomain findById(String clientId) {
//        PaymentEntity entity = mongoRepository.findById(clientId).orElseThrow(
//                () -> new ResourceNotFoundException("Vendedor não foi encontrado."));
//        return mapper.toDomain(entity);
        return null;
    }

    @Override
    public PaymentDomain save(PaymentDomain paymentDomain) {
//        PaymentEntity entity = mapper.toEntity(paymentDomain);
//        PaymentEntity savedEntity = mongoRepository.save(entity);
//        return mapper.toDomain(savedEntity);
        return null;
    }
}