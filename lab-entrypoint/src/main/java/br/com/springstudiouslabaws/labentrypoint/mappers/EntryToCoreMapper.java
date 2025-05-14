package br.com.springstudiouslabaws.labentrypoint.mappers;

import br.com.springstudiouslabaws.labcore.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.labcore.domain.payment.PaymentItemDomain;
import br.com.springstudiouslabaws.labentrypoint.dtos.request.loan.LoanRequest;
import br.com.springstudiouslabaws.labentrypoint.dtos.request.payment.PaymentItemRequest;
import br.com.springstudiouslabaws.labentrypoint.dtos.request.payment.PaymentRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntryToCoreMapper {

    PaymentDomain toDomain(PaymentRequest paymentRequest);

    PaymentItemDomain toDomain(PaymentItemRequest paymentItemRequest);

    LoanDomain toDomain(LoanRequest loanRequest);
}
