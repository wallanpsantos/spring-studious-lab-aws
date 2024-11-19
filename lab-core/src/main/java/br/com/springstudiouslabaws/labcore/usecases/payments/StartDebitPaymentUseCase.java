package br.com.springstudiouslabaws.labcore.usecases.payments;

import br.com.springstudiouslabaws.labcore.domain.payment.StartDebitDomain;
import br.com.springstudiouslabaws.labcore.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class StartDebitPaymentUseCase {

    private final PaymentRepository paymentRepository;

    public StartDebitPaymentUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public StartDebitDomain startDebitPayment(StartDebitDomain startDebit) {
        return paymentRepository.save(startDebit);
    }
}
