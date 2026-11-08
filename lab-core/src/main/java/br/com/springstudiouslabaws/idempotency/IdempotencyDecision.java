package br.com.springstudiouslabaws.idempotency;

/**
 * Resultado de uma tentativa de reservar uma chave de idempotência.
 * STARTED significa que esta chamada ganhou a reserva e deve executar o efeito.
 */
public record IdempotencyDecision(Kind kind, String responseBody) {

    public enum Kind {
        STARTED,
        REPLAY,
        IN_PROGRESS,
        PAYLOAD_MISMATCH
    }

    public static IdempotencyDecision started() {
        return new IdempotencyDecision(Kind.STARTED, null);
    }

    public static IdempotencyDecision fromExisting(String storedHash, String state, String storedBody, String requestHash) {
        if (storedHash == null || !storedHash.equals(requestHash)) {
            return new IdempotencyDecision(Kind.PAYLOAD_MISMATCH, null);
        }
        if ("SUCCEEDED".equals(state)) {
            return new IdempotencyDecision(Kind.REPLAY, storedBody);
        }
        return new IdempotencyDecision(Kind.IN_PROGRESS, null);
    }
}
