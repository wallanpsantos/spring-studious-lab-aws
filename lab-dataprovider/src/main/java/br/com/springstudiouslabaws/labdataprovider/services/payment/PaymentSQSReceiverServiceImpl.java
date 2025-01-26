package br.com.springstudiouslabaws.labdataprovider.services.payment;

import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.labcore.services.sqs.PaymentSQSReceiverService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentSQSReceiverServiceImpl implements PaymentSQSReceiverService {

    private static final Logger log = LoggerFactory.getLogger(PaymentSQSReceiverServiceImpl.class);

    @SqsListener("${sqs.queue.partial}")
    public void receiveFromPartialQueue(PaymentDomain paymentDomain) {
        log.info("Received message from partial payment queue: {}", paymentDomain);
    }

    @SqsListener("${sqs.queue.total}")
    public void receiveFromTotalQueue(PaymentDomain paymentDomain) {
        log.info("Received message from total payment queue: {}", paymentDomain);
    }

    @SqsListener("${sqs.queue.overmeasure}")
    public void receiveFromOvermeasureQueue(PaymentDomain paymentDomain) {
        log.info("Received message from overmeasure payment queue: {}", paymentDomain);
    }

}
