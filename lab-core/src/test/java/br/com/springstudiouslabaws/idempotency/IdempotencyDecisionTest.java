package br.com.springstudiouslabaws.idempotency;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IdempotencyDecisionTest {

    @Test
    void mesmaChaveEMesmoCorpoConcluidoDevolveARespostaGuardada() {
        IdempotencyDecision decision = IdempotencyDecision.fromExisting("hash", "SUCCEEDED", "{\"ok\":true}", "hash");

        assertEquals(IdempotencyDecision.Kind.REPLAY, decision.kind());
        assertEquals("{\"ok\":true}", decision.responseBody());
    }

    @Test
    void mesmaChaveAindaEmAndamentoNaoLiberaOutraExecucao() {
        IdempotencyDecision decision = IdempotencyDecision.fromExisting("hash", "IN_PROGRESS", null, "hash");

        assertEquals(IdempotencyDecision.Kind.IN_PROGRESS, decision.kind());
        assertNull(decision.responseBody());
    }

    @Test
    void mesmaChaveComOutroCorpoFalha() {
        IdempotencyDecision decision = IdempotencyDecision.fromExisting("hash-a", "SUCCEEDED", "{}", "hash-b");

        assertEquals(IdempotencyDecision.Kind.PAYLOAD_MISMATCH, decision.kind());
    }
}
