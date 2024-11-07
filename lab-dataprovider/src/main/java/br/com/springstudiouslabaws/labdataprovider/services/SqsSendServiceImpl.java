package br.com.springstudiouslabaws.labdataprovider.services;

import br.com.springstudiouslabaws.labcore.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.labcore.services.SqsSendService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SqsSendServiceImpl implements SqsSendService {

    private static final Logger log = LoggerFactory.getLogger(SqsSendServiceImpl.class);

    private final SqsTemplate sqsTemplate;

    // Nome das filas específicas
    @Value("${spring.cloud.aws.sqs.queues.partial}")
    private String partialQueue;

    @Value("${spring.cloud.aws.sqs.queues.total}")
    private String totalQueue;

    @Value("${spring.cloud.aws.sqs.queues.overmeasure}")
    private String overmeasureQueue;

    public SqsSendServiceImpl(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    @Override
    public void sendToPartialQueue(PaymentDomain paymentDomain) {
        log.info("Send the item to the partial payment queue: {}", paymentDomain);
        sqsTemplate.send(partialQueue, paymentDomain);
    }

    @Override
    public void sendToTotalQueue(PaymentDomain paymentDomain) {
        log.info("Send the item to the total payment queue: {}", paymentDomain);
        sqsTemplate.send(totalQueue, paymentDomain);
    }

    @Override
    public void sendToOvermeasureQueue(PaymentDomain paymentDomain) {
        log.info("Send the item to the overmeasure payment queue: {}", paymentDomain);
        sqsTemplate.send(overmeasureQueue, paymentDomain);
    }

}