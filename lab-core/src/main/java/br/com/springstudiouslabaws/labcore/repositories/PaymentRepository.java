package br.com.springstudiouslabaws.labcore.repositories;

import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;

public interface PaymentRepository {
    PaymentDomain findById(String id);

    PaymentDomain save(PaymentDomain paymentDomain);
}
