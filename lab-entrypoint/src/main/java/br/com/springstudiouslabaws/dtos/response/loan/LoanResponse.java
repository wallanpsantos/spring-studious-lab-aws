package br.com.springstudiouslabaws.dtos.response.loan;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record LoanResponse(

        @JsonProperty("descricao_emprestimo")
        String description,

        @JsonProperty("valor_solicitado")
        BigDecimal amountRequested,

        @JsonProperty("data_final_vencimento")
        LocalDate dateFinalPayment,

        @JsonProperty("identificador_emprestimo")
        String loanId,

        @JsonProperty("identificador_cliente")
        String clientId,

        @JsonProperty("nome")
        String name,

        @JsonProperty("email")
        String email,

        @JsonProperty("status")
        String status,

        @JsonProperty("atualizado_em")
        LocalDateTime lastUpdate
) {
}
