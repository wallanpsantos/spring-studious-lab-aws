package br.com.springstudiouslabaws.dtos.response.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentItemResponse(

        @JsonProperty("identificador_emprestimo")
        String loanId,

        @JsonProperty("identificador_pagamento")
        String paymentId,

        @JsonProperty("opcao_pagamento")
        String paymentOption
) {
}
