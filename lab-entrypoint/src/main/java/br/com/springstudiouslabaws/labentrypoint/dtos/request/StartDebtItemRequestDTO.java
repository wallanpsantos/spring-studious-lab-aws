package br.com.springstudiouslabaws.labentrypoint.dtos.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record StartDebtItemRequestDTO(

        @NotBlank(message = "amountDebit não pode ser nulo ou vazio")
        BigDecimal amountDebit,

        LocalDate dateFinalDebit) {

}
