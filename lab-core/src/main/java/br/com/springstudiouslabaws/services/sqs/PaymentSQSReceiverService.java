package br.com.springstudiouslabaws.services.sqs;

import br.com.springstudiouslabaws.domain.payment.PaymentDomain;

public interface PaymentSQSReceiverService {
    // Lógica para receber mensagens das filas do SQS

    void receiveFromPartialQueue(PaymentDomain paymentDomain);

    void receiveFromTotalQueue(PaymentDomain paymentDomain);

    void receiveFromOvermeasureQueue(PaymentDomain paymentDomain);
}
