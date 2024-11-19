package br.com.springstudiouslabaws.labentrypoint.dtos.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record StartDebtRequestDTO(

        @NotBlank(message = "Name não pode ser nulo ou vazio")
        String name,

        @NotBlank(message = "O email não pode ser nulo ou vazio.")
        @Email(message = "O email deve ser válido.")
        String email,

        @Valid
        List<StartDebtItemRequestDTO> debts) {

}
