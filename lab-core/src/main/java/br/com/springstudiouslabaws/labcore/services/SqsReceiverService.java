package br.com.springstudiouslabaws.labcore.services;

import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;

public interface SqsReceiverService {
    // Lógica para receber mensagens das filas do SQS

    void receiveFromPartialQueue(PaymentDomain paymentDomain);

    void receiveFromTotalQueue(PaymentDomain paymentDomain);

    void receiveFromOvermeasureQueue(PaymentDomain paymentDomain);
}
