package br.com.springstudiouslabaws.labentrypoint.dtos.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PaymentItemResponseDTO(
        String paymentId,
        BigDecimal paymentValue,
        String paymentStatus
) {
}
