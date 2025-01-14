package br.com.springstudiouslabaws.labentrypoint.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonNaming(SnakeCaseStrategy.class)
public record LoanRequestDTO(

        @NotBlank(message = "Identificador do cliente não pode ser nulo ou vazio")
        @JsonProperty("identificador_cliente")
        String clientId,

        @NotBlank(message = "Nome do cliente não pode ser nulo ou vazio")
        @JsonProperty("nome")
        String name,

        @NotBlank(message = "E-mail do cliente não pode ser nulo ou vazio")
        @JsonProperty("email")
        String email,

        @NotBlank(message = "Descrição do empréstimo não pode ser nulo ou vazio")
        @JsonProperty("descricao_emprestimo")
        String description,

        @NotBlank(message = "Valor solicitado do empréstimo não pode ser nulo ou vazio")
        @JsonProperty("valor_solicitado")
        BigDecimal amountRequested,

        @NotBlank(message = "Data de vencimento do empréstimo não pode ser nulo ou vazio")
        @JsonProperty("data_final_vencimento")
        LocalDate dateFinalPayment
) {
}
