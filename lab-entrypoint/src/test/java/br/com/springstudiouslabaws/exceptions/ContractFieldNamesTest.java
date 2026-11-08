package br.com.springstudiouslabaws.exceptions;

import br.com.springstudiouslabaws.dtos.request.payment.PaymentRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContractFieldNamesTest {

    @Test
    void traduzCampoAninhadoParaONomeDoJson() {
        String path = ContractFieldNames.toJsonPath(PaymentRequest.class, "paymentItems[0].loanId");

        assertEquals("pagamentos[0].identificador_emprestimo", path);
    }
}
