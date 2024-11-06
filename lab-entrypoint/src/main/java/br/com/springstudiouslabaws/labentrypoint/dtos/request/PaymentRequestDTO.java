package br.com.springstudiouslabaws.labentrypoint.dtos.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@JsonNaming(SnakeCaseStrategy.class)
public record PaymentRequestDTO(

        @NotBlank(message = "ClientId não pode ser nulo ou vazio")
        String clientId,

        @Valid
        List<PaymentItemRequestDTO> paymentItems
) {
}
