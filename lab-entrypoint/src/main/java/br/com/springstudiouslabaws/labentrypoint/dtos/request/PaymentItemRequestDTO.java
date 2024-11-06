package br.com.springstudiouslabaws.labentrypoint.dtos.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

@JsonNaming(SnakeCaseStrategy.class)
public record PaymentItemRequestDTO(

        @NotBlank(message = "PaymentId não pode ser nulo ou vazio")
        String paymentId,

        BigDecimal paymentValue,
        String paymentStatus
) {
}
