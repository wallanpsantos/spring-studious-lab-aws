package br.com.springstudiouslabaws.dtos.request.loan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanRequest(

        @NotBlank(message = "O identificador do cliente não pode estar em branco")
        @JsonProperty("identificador_cliente")
        String clientId,

        @NotBlank(message = "O nome não pode estar em branco")
        @JsonProperty("nome")
        String name,

        @NotBlank(message = "O email não pode estar em branco")
        @JsonProperty("email")
        String email,

        @JsonProperty("descricao_emprestimo")
        String description,

        @NotBlank(message = "O valor solicitado não pode estar em branco")
        @JsonProperty("valor_solicitado")
        BigDecimal amountRequested,

        @NotBlank(message = "A data final de vencimento não pode estar em branco")
        @JsonFormat(pattern = "[dd/MM/yyyy][dd-MM-yyyy][yyyy-MM-dd]") /// aceita ambos formatos de data (27/04/2025, 27-04-2025 ou 2025-12-29)
        @JsonProperty("data_final_vencimento")
        LocalDate dateFinalPayment

) {
}