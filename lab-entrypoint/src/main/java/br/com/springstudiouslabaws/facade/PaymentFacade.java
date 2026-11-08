package br.com.springstudiouslabaws.facade;

import br.com.springstudiouslabaws.dtos.request.payment.PaymentRequest;
import br.com.springstudiouslabaws.dtos.response.payment.PaymentResponse;
import br.com.springstudiouslabaws.idempotency.IdempotentCommand;
import br.com.springstudiouslabaws.mappers.DomainToDTOMapper;
import br.com.springstudiouslabaws.mappers.EntryToCoreMapper;
import br.com.springstudiouslabaws.usecases.payment.CreatePaymentUseCase;
import org.springframework.stereotype.Component;

@Component
public class PaymentFacade {

    static final String SCOPE = "pagamentos";

    private final CreatePaymentUseCase createPaymentUseCase;
    private final EntryToCoreMapper entryToCoreMapper;
    private final DomainToDTOMapper domainToDTOMapper;
    private final IdempotentCommand idempotentCommand;

    public PaymentFacade(CreatePaymentUseCase createPaymentUseCase,
                         EntryToCoreMapper entryToCoreMapper,
                         DomainToDTOMapper domainToDTOMapper,
                         IdempotentCommand idempotentCommand) {
        this.createPaymentUseCase = createPaymentUseCase;
        this.entryToCoreMapper = entryToCoreMapper;
        this.domainToDTOMapper = domainToDTOMapper;
        this.idempotentCommand = idempotentCommand;
    }

    public PaymentResponse createPayment(String idempotencyKey, PaymentRequest request) {
        return idempotentCommand.execute(SCOPE, idempotencyKey, request, PaymentResponse.class, () -> {
            var processed = createPaymentUseCase.process(entryToCoreMapper.toDomain(request));
            return domainToDTOMapper.toDTO(processed);
        });
    }
}
