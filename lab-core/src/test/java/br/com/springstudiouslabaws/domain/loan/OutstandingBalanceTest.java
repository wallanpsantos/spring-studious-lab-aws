package br.com.springstudiouslabaws.domain.loan;

import br.com.springstudiouslabaws.exceptions.BusinessException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OutstandingBalanceTest {

    @Test
    void baixaParcialReduzOSaldo() {
        BigDecimal saldo = OutstandingBalance.afterDebit(
                new BigDecimal("1000.00"), new BigDecimal("1000.00"), new BigDecimal("150.50"));

        assertEquals(new BigDecimal("849.50"), saldo);
    }

    @Test
    void saldoAusenteParteDoValorSolicitado() {
        BigDecimal saldo = OutstandingBalance.afterDebit(
                null, new BigDecimal("500.00"), new BigDecimal("100.00"));

        assertEquals(new BigDecimal("400.00"), saldo);
    }

    @Test
    void baixaIgualAoSaldoZera() {
        BigDecimal saldo = OutstandingBalance.afterDebit(
                new BigDecimal("80.00"), new BigDecimal("80.00"), new BigDecimal("80.00"));

        assertEquals(0, saldo.compareTo(BigDecimal.ZERO));
    }

    @Test
    void baixaMaiorQueOSaldoFalha() {
        BusinessException erro = assertThrows(BusinessException.class, () -> OutstandingBalance.afterDebit(
                new BigDecimal("10.00"), new BigDecimal("10.00"), new BigDecimal("10.01")));

        assertEquals("Baixa maior que o saldo do empréstimo", erro.getMessage());
    }

    @Test
    void baixaNulaFalha() {
        BusinessException erro = assertThrows(BusinessException.class, () -> OutstandingBalance.afterDebit(
                new BigDecimal("10.00"), new BigDecimal("10.00"), null));

        assertEquals("Valor da baixa deve ser maior que zero", erro.getMessage());
    }

    @Test
    void baixaZeroOuNegativaFalha() {
        assertThrows(BusinessException.class, () -> OutstandingBalance.afterDebit(
                new BigDecimal("10.00"), new BigDecimal("10.00"), BigDecimal.ZERO));
        assertThrows(BusinessException.class, () -> OutstandingBalance.afterDebit(
                new BigDecimal("10.00"), new BigDecimal("10.00"), new BigDecimal("-1")));
    }

    @Test
    void saldoIndefinidoFalha() {
        BusinessException erro = assertThrows(BusinessException.class, () -> OutstandingBalance.afterDebit(
                null, null, new BigDecimal("1.00")));

        assertEquals("Saldo do empréstimo não está definido", erro.getMessage());
    }
}
