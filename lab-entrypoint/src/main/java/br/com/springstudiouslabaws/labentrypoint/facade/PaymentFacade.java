package br.com.springstudiouslabaws.labentrypoint.facade;

import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.labcore.domain.payment.StartDebitDomain;
import br.com.springstudiouslabaws.labcore.usecases.payments.ProcessPaymentUseCase;
import br.com.springstudiouslabaws.labcore.usecases.payments.StartDebitPaymentUseCase;
import br.com.springstudiouslabaws.labentrypoint.dtos.request.PaymentRequestDTO;
import br.com.springstudiouslabaws.labentrypoint.dtos.request.StartDebtRequestDTO;
import br.com.springstudiouslabaws.labentrypoint.dtos.response.PaymentResponseDTO;
import br.com.springstudiouslabaws.labentrypoint.mappers.DomainToDTOMapper;
import br.com.springstudiouslabaws.labentrypoint.mappers.EntryToCoreMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class PaymentFacade {

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final StartDebitPaymentUseCase startDebitPaymentUseCase;
    private final EntryToCoreMapper entryToCoreMapper;
    private final DomainToDTOMapper domainToDTOMapper;

    public PaymentFacade(ProcessPaymentUseCase processPaymentUseCase, StartDebitPaymentUseCase startDebitPaymentUseCase, EntryToCoreMapper entryToCoreMapper, DomainToDTOMapper domainToDTOMapper) {
        this.processPaymentUseCase = processPaymentUseCase;
        this.startDebitPaymentUseCase = startDebitPaymentUseCase;
        this.entryToCoreMapper = entryToCoreMapper;
        this.domainToDTOMapper = domainToDTOMapper;
    }

    public PaymentResponseDTO processPayment(PaymentRequestDTO requestDTO) {
        PaymentDomain paymentDomain = entryToCoreMapper.toDomain(requestDTO);
        PaymentDomain processedPayment = processPaymentUseCase.processPayment(paymentDomain);
        return domainToDTOMapper.toDTO(processedPayment);
    }

    public PaymentResponseDTO createPayment(@Valid StartDebtRequestDTO requestDTO) {
        StartDebitDomain paymentDomain = entryToCoreMapper.toDomain(requestDTO);
        var r = startDebitPaymentUseCase.startDebitPayment(StartDebitDomain);


        return null;
    }
}
