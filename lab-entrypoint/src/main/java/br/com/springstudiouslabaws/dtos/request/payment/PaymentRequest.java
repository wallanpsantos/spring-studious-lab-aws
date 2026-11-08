package br.com.springstudiouslabaws.dtos.request.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PaymentRequest(

        @NotBlank(message = "O identificador do cliente não pode estar em branco")
        @JsonProperty("identificador_cliente")
        String clientId,

        @NotEmpty(message = "Informe ao menos um pagamento")
        @JsonProperty("pagamentos")
        List<@Valid PaymentItemRequest> paymentItems
) {
}
