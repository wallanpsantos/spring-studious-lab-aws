package br.com.springstudiouslabaws.services.sqs;

import br.com.springstudiouslabaws.domain.payment.PaymentDomain;

public interface PaymentSQSSendService {

    void sendToPartialQueue(PaymentDomain paymentDomain);
    // Lógica para envio ao SQS da fila parcial

    void sendToTotalQueue(PaymentDomain paymentDomain);
    // Lógica para envio ao SQS da fila total

    void sendToOvermeasureQueue(PaymentDomain paymentDomain);
    // Lógica para envio ao SQS da fila excedente
}
