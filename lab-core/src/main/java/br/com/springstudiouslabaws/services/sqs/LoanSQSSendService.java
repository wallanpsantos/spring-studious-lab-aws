package br.com.springstudiouslabaws.services.sqs;

import br.com.springstudiouslabaws.domain.loan.LoanDomain;

public interface LoanSQSSendService {

    void sendLoan(LoanDomain loanDomain);
    // Lógica para envio ao SQS do empréstimo solicitado
}
