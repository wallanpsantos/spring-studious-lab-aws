package br.com.springstudiouslabaws.domain.loan;

import java.math.BigDecimal;

public record LoanSettlement(BigDecimal outstandingBalance, String status) {
}
