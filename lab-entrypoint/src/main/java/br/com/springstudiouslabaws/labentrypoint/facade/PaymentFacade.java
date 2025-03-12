package br.com.springstudiouslabaws.labentrypoint.facade;

import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.labcore.usecases.payment.CreatePaymentUseCase;
import br.com.springstudiouslabaws.labentrypoint.dtos.request.payment.PaymentRequest;
import br.com.springstudiouslabaws.labentrypoint.dtos.response.payment.PaymentResponse;
import br.com.springstudiouslabaws.labentrypoint.mappers.DomainToDTOMapper;
import br.com.springstudiouslabaws.labentrypoint.mappers.EntryToCoreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PaymentFacade {

    private static final Logger log = LoggerFactory.getLogger(PaymentFacade.class);

    private final CreatePaymentUseCase createPaymentUseCase;
    private final EntryToCoreMapper entryToCoreMapper;
    private final DomainToDTOMapper domainToDTOMapper;

    public PaymentFacade(CreatePaymentUseCase createPaymentUseCase,
                         EntryToCoreMapper entryToCoreMapper,
                         DomainToDTOMapper domainToDTOMapper) {
        this.createPaymentUseCase = createPaymentUseCase;
        this.entryToCoreMapper = entryToCoreMapper;
        this.domainToDTOMapper = domainToDTOMapper;
    }

    public PaymentResponse createPayment(PaymentRequest request) {
        log.info("Criando novo pagamento para o cliente: {}", request.clientId());

        PaymentDomain domain = entryToCoreMapper.toDomain(request);

        PaymentDomain processedPayment = createPaymentUseCase.process(domain);

        PaymentResponse response = domainToDTOMapper.toDTO(processedPayment);

        log.info("Novo pagamento criado com sucesso para o cliente: {}", response.clientId());
        return response;
    }

}
