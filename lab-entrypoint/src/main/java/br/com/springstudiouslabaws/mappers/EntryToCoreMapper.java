package br.com.springstudiouslabaws.mappers;

import br.com.springstudiouslabaws.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.domain.payment.PaymentItemDomain;
import br.com.springstudiouslabaws.dtos.request.loan.LoanRequest;
import br.com.springstudiouslabaws.dtos.request.payment.PaymentItemRequest;
import br.com.springstudiouslabaws.dtos.request.payment.PaymentRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntryToCoreMapper {

    PaymentDomain toDomain(PaymentRequest paymentRequest);

    PaymentItemDomain toDomain(PaymentItemRequest paymentItemRequest);

    LoanDomain toDomain(LoanRequest loanRequest);
}
