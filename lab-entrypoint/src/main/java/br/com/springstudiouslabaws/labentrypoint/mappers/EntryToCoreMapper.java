package br.com.springstudiouslabaws.labentrypoint.mappers;

import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.labcore.domain.payment.StartDebitDomain;
import br.com.springstudiouslabaws.labentrypoint.dtos.request.PaymentRequestDTO;
import br.com.springstudiouslabaws.labentrypoint.dtos.request.StartDebtRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntryToCoreMapper {

    PaymentDomain toDomain(PaymentRequestDTO requestDTO);

    @Mapping(target = "clientName", source = "name")
    @Mapping(target = "clientEmail", source = "email")
    @Mapping(target = "payments.dateFinalPayment", source = "debts.dateFinalDebit")
    @Mapping(target = "payments.originalAmount", source = "debts.amountDebit")
    StartDebitDomain toDomain(StartDebtRequestDTO requestDTO);

}
