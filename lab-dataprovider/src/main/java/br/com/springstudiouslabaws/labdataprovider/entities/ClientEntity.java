package br.com.springstudiouslabaws.labdataprovider.entities;

public record ClientEntity(
        String clientId,
        String clientName,
        String clientEmail
) {
}