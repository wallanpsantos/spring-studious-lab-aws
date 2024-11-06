package br.com.springstudiouslabaws.labdataprovider.mappers;

import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.labdataprovider.entities.PaymentEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface CoreToDataMapper {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE) // ignorar campos não mapeados e a não emitir avisos.
    @Mapping(source = "paymentItems", target = "payments")
    PaymentEntity toEntity(PaymentDomain domain);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE) // ignorar campos não mapeados e a não emitir avisos.
    @Mapping(source = "payments", target = "paymentItems")
    PaymentDomain toDomain(PaymentEntity entity);
}

