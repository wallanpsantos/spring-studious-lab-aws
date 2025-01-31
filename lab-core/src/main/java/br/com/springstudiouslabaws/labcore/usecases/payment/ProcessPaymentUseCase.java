package br.com.springstudiouslabaws.labcore.usecases.payment;

import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.labcore.enums.PaymentItemStatusEnum;
import br.com.springstudiouslabaws.labcore.exceptions.PaymentNotFoundException;
import br.com.springstudiouslabaws.labcore.repositories.PaymentRepository;
import br.com.springstudiouslabaws.labcore.services.sqs.PaymentSQSSendService;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static br.com.springstudiouslabaws.labcore.enums.PaymentItemStatusEnum.fromString;

@Service
public class ProcessPaymentUseCase {

    private final PaymentRepository repository;
    private final PaymentSQSSendService sqsService;

    public ProcessPaymentUseCase(PaymentRepository repository, PaymentSQSSendService sqsService) {
        this.repository = repository;
        this.sqsService = sqsService;
    }

    public PaymentDomain processPayment(PaymentDomain paymentDomain) {

        PaymentDomain payment = repository.findById(paymentDomain.getClientId());

        validatePaymentItems(paymentDomain, payment);

        sendToQueueBasedOnStatus(paymentDomain);

        return repository.save(paymentDomain);
    }

    private void validatePaymentItems(PaymentDomain paymentDomain, PaymentDomain payments) {
        if (!Objects.equals(payments.getPaymentItems().getFirst().getPaymentId(), paymentDomain.getPaymentItems().getFirst().getPaymentId())) {
            throw new PaymentNotFoundException("Código da cobrança não foi encontrado.");
        }
    }

    private void sendToQueueBasedOnStatus(PaymentDomain payment) {
        PaymentItemStatusEnum status = fromString(payment.getPaymentItems().getFirst().getPaymentStatus());
        switch (status) {
            case PARTIAL -> sqsService.sendToPartialQueue(payment);
            case TOTAL -> sqsService.sendToTotalQueue(payment);
            case OVERMEASURE -> sqsService.sendToOvermeasureQueue(payment);
        }
    }
}

