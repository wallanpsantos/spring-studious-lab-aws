package br.com.springstudiouslabaws.labentrypoint.dtos.request.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@JsonNaming(SnakeCaseStrategy.class)
public record PaymentItemRequest(

        @JsonProperty("identificador_emprestimo")
        String loanId,

        @JsonProperty("valor_pagamento")
        BigDecimal paymentValue,

        @JsonProperty("comentario")
        String comment
) {
}
