package br.com.springstudiouslabaws.dtos.response.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PaymentResponse(

        @JsonProperty("identificador_cliente")
        String clientId,

        @JsonProperty("pagamentos_efetuados")
        List<PaymentItemResponse> paymentItems
) {

}
