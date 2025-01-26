package br.com.springstudiouslabaws.labdataprovider.services.loan;

import br.com.springstudiouslabaws.labcore.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.labcore.services.sqs.LoanSQSSendService;
import org.springframework.stereotype.Service;

@Service
public class LoanSQSSendServiceImpl implements LoanSQSSendService {

    @Override
    public void sendLoan(LoanDomain loanDomain) {

    }
}
