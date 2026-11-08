package br.com.springstudiouslabaws.entities;

public record ClientEntity(
        String clientId,
        String clientName,
        String clientEmail
) {
}