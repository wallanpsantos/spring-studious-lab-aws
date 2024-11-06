package br.com.springstudiouslabaws.labentrypoint.dtos.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PaymentResponseDTO(
        String clientId,
        List<PaymentItemResponseDTO> paymentItems
) {

}
