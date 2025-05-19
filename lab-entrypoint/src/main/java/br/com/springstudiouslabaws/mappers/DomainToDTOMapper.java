package br.com.springstudiouslabaws.mappers;

import br.com.springstudiouslabaws.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.dtos.response.loan.LoanResponse;
import br.com.springstudiouslabaws.dtos.response.payment.PaymentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DomainToDTOMapper {

    PaymentResponse toDTO(PaymentDomain paymentDomain);

    LoanResponse toDTO(LoanDomain loanDomain);
}

