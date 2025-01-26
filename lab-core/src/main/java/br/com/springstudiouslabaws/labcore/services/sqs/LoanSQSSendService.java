package br.com.springstudiouslabaws.labcore.services.sqs;

import br.com.springstudiouslabaws.labcore.domain.loan.LoanDomain;

public interface LoanSQSSendService {

    void sendLoan(LoanDomain loanDomain);
    // Lógica para envio ao SQS do empréstimo solicitado
}
