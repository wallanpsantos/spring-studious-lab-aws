package br.com.springstudiouslabaws.dtos.request.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentItemRequest(

        @NotBlank(message = "O identificador do empréstimo não pode estar em branco")
        @JsonProperty("identificador_emprestimo")
        String loanId,

        @NotNull(message = "O valor do pagamento é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor do pagamento deve ser maior que zero")
        @JsonProperty("valor_pagamento")
        BigDecimal paymentValue,

        @JsonProperty("comentario")
        String comment
) {
}
