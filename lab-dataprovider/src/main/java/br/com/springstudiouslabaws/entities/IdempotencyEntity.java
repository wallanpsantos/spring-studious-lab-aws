package br.com.springstudiouslabaws.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "idempotency_keys")
public record IdempotencyEntity(
        @Id String id,
        String requestHash,
        String state,
        String responseBody,
        Instant createdAt
) {
}
