package br.com.springstudiouslabaws.labentrypoint.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanRequestDTO(

        @NotBlank(message = "Identificador do cliente não pode ser nulo ou vazio")
        @JsonProperty("identificador_cliente")
        String clientId,

        @NotBlank(message = "Nome do cliente não pode ser nulo ou vazio")
        @JsonProperty("nome")
        String name,

        @NotBlank(message = "E-mail do cliente não pode ser nulo ou vazio")
        @Email(message = "E-mail inválido")
        @JsonProperty("email")
        String email,

        @NotBlank(message = "Descrição do empréstimo não pode ser nulo ou vazio")
        @JsonProperty("descricao_emprestimo")
        String description,

        @NotNull(message = "Valor solicitado do empréstimo não pode ser nulo")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        @JsonProperty("valor_solicitado")
        BigDecimal amountRequested,

        @NotNull(message = "Data de vencimento do empréstimo não pode ser nula")
        @Future(message = "Data de vencimento deve ser uma data futura")
        @JsonProperty("data_final_vencimento")
        LocalDate dateFinalPayment
) {
}