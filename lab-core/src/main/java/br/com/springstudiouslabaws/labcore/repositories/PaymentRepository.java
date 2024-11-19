package br.com.springstudiouslabaws.labcore.repositories;

import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.labcore.domain.payment.StartDebitDomain;

public interface PaymentRepository {
    PaymentDomain findById(String id);

    PaymentDomain save(PaymentDomain paymentDomain);

    StartDebitDomain save(StartDebitDomain startDebitDomain);
}
