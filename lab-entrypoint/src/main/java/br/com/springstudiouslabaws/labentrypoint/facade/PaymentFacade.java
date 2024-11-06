package br.com.springstudiouslabaws.labentrypoint.facade;

import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.labcore.usecases.ProcessPaymentUseCase;
import br.com.springstudiouslabaws.labentrypoint.dtos.request.PaymentRequestDTO;
import br.com.springstudiouslabaws.labentrypoint.dtos.response.PaymentResponseDTO;
import br.com.springstudiouslabaws.labentrypoint.mappers.DomainToDTOMapper;
import br.com.springstudiouslabaws.labentrypoint.mappers.EntryToCoreMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class PaymentFacade {

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final EntryToCoreMapper entryToCoreMapper;
    private final DomainToDTOMapper domainToDTOMapper;

    public PaymentFacade(ProcessPaymentUseCase processPaymentUseCase, EntryToCoreMapper entryToCoreMapper, DomainToDTOMapper domainToDTOMapper) {
        this.processPaymentUseCase = processPaymentUseCase;
        this.entryToCoreMapper = entryToCoreMapper;
        this.domainToDTOMapper = domainToDTOMapper;
    }

    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequestDTO) {
        PaymentDomain paymentDomain = entryToCoreMapper.toDomain(paymentRequestDTO);
        PaymentDomain processedPayment = processPaymentUseCase.processPayment(paymentDomain);
        return domainToDTOMapper.toDTO(processedPayment);
    }

    public PaymentResponseDTO createPayment(@Valid PaymentRequestDTO request) {
        return null;
    }
}
