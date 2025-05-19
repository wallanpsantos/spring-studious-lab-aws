package br.com.springstudiouslabaws.usecases.payment;

import br.com.springstudiouslabaws.domain.payment.PaymentDomain;
import br.com.springstudiouslabaws.repositories.LoanRepository;
import br.com.springstudiouslabaws.repositories.PaymentRepository;
import br.com.springstudiouslabaws.services.sqs.PaymentSQSSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreatePaymentUseCase {

    private final Logger log = LoggerFactory.getLogger(CreatePaymentUseCase.class);

    private final PaymentRepository paymentRepository;
    private final LoanRepository loanRepository;
    private final PaymentSQSSendService sqsService;

    public CreatePaymentUseCase(LoanRepository loanRepository, PaymentRepository paymentRepository, PaymentSQSSendService sqsService) {
        this.loanRepository = loanRepository;
        this.paymentRepository = paymentRepository;
        this.sqsService = sqsService;
    }

    public PaymentDomain process(PaymentDomain paymentDomain) {
        log.info("Processando pagamento para cliente: {}", paymentDomain.getClientId());


        return paymentRepository.save(paymentDomain);
    }

}

