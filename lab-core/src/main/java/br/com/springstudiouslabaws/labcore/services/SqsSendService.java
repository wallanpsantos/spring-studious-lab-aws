package br.com.springstudiouslabaws.labcore.services;

import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;

public interface SqsSendService {

    void sendToPartialQueue(PaymentDomain paymentDomain);
    // Lógica para envio ao SQS da fila parcial

    void sendToTotalQueue(PaymentDomain paymentDomain);
    // Lógica para envio ao SQS da fila total

    void sendToOvermeasureQueue(PaymentDomain paymentDomain);
    // Lógica para envio ao SQS da fila excedente
}