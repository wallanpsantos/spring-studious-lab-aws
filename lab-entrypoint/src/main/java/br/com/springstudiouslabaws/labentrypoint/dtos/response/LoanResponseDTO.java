package br.com.springstudiouslabaws.labentrypoint.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanResponseDTO(

        @JsonProperty("descricao_emprestimo")
        String description,

        @JsonProperty("valor_solicitado")
        BigDecimal amountRequested,

        @JsonProperty("data_final_vencimento")
        LocalDate dateFinalPayment
) {
}
