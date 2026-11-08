package br.com.springstudiouslabaws.domain.loan;

import br.com.springstudiouslabaws.enums.LoanStatusEnum;
import br.com.springstudiouslabaws.exceptions.BusinessException;

import java.math.BigDecimal;

public final class OutstandingBalance {

    private OutstandingBalance() {
    }

    public static LoanSettlement afterDebit(BigDecimal outstanding, BigDecimal amountRequested, BigDecimal debit) {
        if (debit == null || debit.signum() <= 0) {
            throw new BusinessException("Valor da baixa deve ser maior que zero");
        }
        BigDecimal current = outstanding != null ? outstanding : amountRequested;
        if (current == null) {
            throw new BusinessException("Saldo do empréstimo não está definido");
        }
        if (debit.compareTo(current) > 0) {
            throw new BusinessException("Baixa maior que o saldo do empréstimo");
        }
        BigDecimal next = current.subtract(debit);
        String status = next.signum() == 0
                ? LoanStatusEnum.PAID.getValue()
                : LoanStatusEnum.PENDING.getValue();
        return new LoanSettlement(next, status);
    }
}
