package br.com.springstudiouslabaws.dtos.request.loan;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanRequest(

        @NotBlank(message = "O identificador do cliente não pode estar em branco")
        @JsonProperty("identificador_cliente")
        String clientId,

        @NotBlank(message = "O nome não pode estar em branco")
        @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
        @JsonProperty("nome")
        String name,

        @NotBlank(message = "O email não pode estar em branco")
        @Email(message = "E-mail inválido")
        @JsonProperty("email")
        String email,

        @NotBlank(message = "A descrição do empréstimo não pode estar em branco")
        @JsonProperty("descricao_emprestimo")
        String description,

        @NotNull(message = "O valor solicitado é obrigatório")
        @DecimalMin(value = "100.00", message = "O valor solicitado deve ser de pelo menos 100.00")
        @DecimalMax(value = "1000000.00", message = "O valor solicitado não pode passar de 1000000.00")
        @JsonProperty("valor_solicitado")
        BigDecimal amountRequested,

        @NotNull(message = "A data final de vencimento é obrigatória")
        @JsonFormat(pattern = "[dd/MM/yyyy][dd-MM-yyyy][yyyy-MM-dd]")
        @JsonProperty("data_final_vencimento")
        LocalDate dateFinalPayment

) {
}
