package br.com.springstudiouslabaws.labentrypoint.mappers;

import br.com.springstudiouslabaws.labcore.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.labentrypoint.dtos.response.loan.LoanResponse;
import br.com.springstudiouslabaws.labentrypoint.dtos.response.payment.PaymentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DomainToDTOMapper {

    PaymentResponse toDTO(PaymentDomain paymentDomain);

    LoanResponse toDTO(LoanDomain loanDomain);
}

