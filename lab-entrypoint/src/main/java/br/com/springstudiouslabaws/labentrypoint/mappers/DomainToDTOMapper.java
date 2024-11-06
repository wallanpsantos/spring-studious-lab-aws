package br.com.springstudiouslabaws.labentrypoint.mappers;

import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.labentrypoint.dtos.response.PaymentResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DomainToDTOMapper {

    PaymentResponseDTO toDTO(PaymentDomain paymentDomain);
}

