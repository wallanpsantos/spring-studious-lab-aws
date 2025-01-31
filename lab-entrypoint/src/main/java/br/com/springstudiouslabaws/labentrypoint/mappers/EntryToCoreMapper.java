package br.com.springstudiouslabaws.labentrypoint.mappers;

import br.com.springstudiouslabaws.labcore.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.labentrypoint.dtos.request.LoanRequestDTO;
import br.com.springstudiouslabaws.labentrypoint.dtos.request.PaymentRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntryToCoreMapper {

    PaymentDomain toDomain(PaymentRequestDTO paymentRequestDTO);

    LoanDomain toDomain(LoanRequestDTO loanRequestDTO);
}
