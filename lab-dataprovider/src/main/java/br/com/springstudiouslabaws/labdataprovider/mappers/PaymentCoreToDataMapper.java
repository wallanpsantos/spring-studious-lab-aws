package br.com.springstudiouslabaws.labdataprovider.mappers;

import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.labdataprovider.entities.PaymentsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", // Define que o mapper será gerenciado pelo Spring Framework
        unmappedTargetPolicy = ReportingPolicy.IGNORE, // ignorar campos não mapeados e a não emitir avisos.
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) // não mapear nulo do objeto fonte
public interface PaymentCoreToDataMapper {

    //    @Mapping(source = "paymentItems", target = "payments")
    PaymentsEntity toEntity(PaymentDomain domain);

    //    @Mapping(source = "payments", target = "paymentItems")
    PaymentDomain toDomain(PaymentsEntity entity);
}

