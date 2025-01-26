package br.com.springstudiouslabaws.labcore.usecases.loan;

import br.com.springstudiouslabaws.labcore.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.labcore.enums.LoanStatusEnum;
import br.com.springstudiouslabaws.labcore.repositories.LoanRepository;
import br.com.springstudiouslabaws.labcore.services.sqs.LoanSQSSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LoanUseCase {

    private static final Logger log = LoggerFactory.getLogger(LoanUseCase.class);

    private final LoanRepository repository;
    private final LoanSQSSendService loanSQSSendService;

    public LoanUseCase(LoanRepository loanRepository, LoanSQSSendService loanSQSSendService) {
        this.repository = loanRepository;
        this.loanSQSSendService = loanSQSSendService;
    }

    public LoanDomain processLoan(LoanDomain loanDomain) {

        loanDomain.validate();
        loanDomain.setLastUpdate(LocalDateTime.now());
        loanDomain.setLoanId(UUID.randomUUID().toString());
        loanDomain.setStatus(validateStatus(loanDomain.getStatus()));

        LoanDomain loan = repository.save(loanDomain);

        log.info("Empréstimo salvo: {}", loan);

        return loan;
    }

    private String validateStatus(String status) {
        return status == null ? LoanStatusEnum.PENDING.getValue() : status;
    }
}
