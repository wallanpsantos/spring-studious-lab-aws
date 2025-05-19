package br.com.springstudiouslabaws.services.loan;

import br.com.springstudiouslabaws.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.services.sqs.LoanSQSSendService;
import org.springframework.stereotype.Service;

@Service
public class LoanSQSSendServiceImpl implements LoanSQSSendService {

    @Override
    public void sendLoan(LoanDomain loanDomain) {

    }
}
