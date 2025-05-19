package br.com.springstudiouslabaws.repositories;

import br.com.springstudiouslabaws.domain.payment.PaymentDomain;

public interface PaymentRepository {

    PaymentDomain findById(String id);

    PaymentDomain save(PaymentDomain paymentDomain);
}
