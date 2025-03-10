package br.com.springstudiouslabaws.labentrypoint.dtos.request.loan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanRequest(

        @JsonProperty("identificador_cliente")
        String clientId,

        @JsonProperty("nome")
        String name,

        @JsonProperty("email")
        String email,

        @JsonProperty("descricao_emprestimo")
        String description,

        @JsonProperty("valor_solicitado")
        BigDecimal amountRequested,

        @JsonFormat(pattern = "[dd/MM/yyyy][dd-MM-yyyy][yyyy-MM-dd]") /// aceita ambos formatos de data (27/04/2025, 27-04-2025 ou 2025-12-29)
        @JsonProperty("data_final_vencimento")
        LocalDate dateFinalPayment
) {
}