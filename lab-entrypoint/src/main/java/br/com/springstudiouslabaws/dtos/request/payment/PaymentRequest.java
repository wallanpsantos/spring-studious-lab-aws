package br.com.springstudiouslabaws.dtos.request.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(SnakeCaseStrategy.class)
public record PaymentRequest(

        @JsonProperty("identificador_cliente")
        String clientId,

        @JsonProperty("pagamentos")
        List<PaymentItemRequest> paymentItems
) {
}
