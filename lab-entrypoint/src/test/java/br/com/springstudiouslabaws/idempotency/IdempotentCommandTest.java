package br.com.springstudiouslabaws.idempotency;

import br.com.springstudiouslabaws.exceptions.IdempotencyConflictException;
import br.com.springstudiouslabaws.exceptions.IdempotencyPayloadMismatchException;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IdempotentCommandTest {

    @Test
    void semChaveExecutaTodaVez() {
        RecordingStore store = new RecordingStore();
        IdempotentCommand command = new IdempotentCommand(store);
        AtomicInteger runs = new AtomicInteger();

        command.execute("emprestimos", null, Map.of("a", 1), String.class, () -> "r" + runs.incrementAndGet());
        String second = command.execute("emprestimos", " ", Map.of("a", 1), String.class, () -> "r" + runs.incrementAndGet());

        assertEquals("r2", second);
        assertEquals(0, store.begins);
    }

    @Test
    void primeiraChaveExecutaEGuardaAResposta() {
        RecordingStore store = new RecordingStore();
        IdempotentCommand command = new IdempotentCommand(store);

        String response = command.execute("emprestimos", "chave-1", Map.of("a", 1), String.class, () -> "criado");

        assertEquals("criado", response);
        assertEquals(1, store.completes);
    }

    @Test
    void retentativaComOMesmoCorpoNaoExecutaDeNovo() {
        RecordingStore store = new RecordingStore();
        store.next = new IdempotencyDecision(IdempotencyDecision.Kind.REPLAY, "\"criado\"");
        IdempotentCommand command = new IdempotentCommand(store);
        AtomicInteger runs = new AtomicInteger();

        String response = command.execute("emprestimos", "chave-1", Map.of("a", 1), String.class, () -> {
            runs.incrementAndGet();
            return "novo";
        });

        assertEquals("criado", response);
        assertEquals(0, runs.get());
    }

    @Test
    void chaveEmAndamentoRespondeConflito() {
        RecordingStore store = new RecordingStore();
        store.next = new IdempotencyDecision(IdempotencyDecision.Kind.IN_PROGRESS, null);
        IdempotentCommand command = new IdempotentCommand(store);

        assertThrows(IdempotencyConflictException.class,
                () -> command.execute("emprestimos", "chave-1", Map.of("a", 1), String.class, () -> "novo"));
    }

    @Test
    void outroCorpoNaMesmaChaveFalha() {
        RecordingStore store = new RecordingStore();
        store.next = new IdempotencyDecision(IdempotencyDecision.Kind.PAYLOAD_MISMATCH, null);
        IdempotentCommand command = new IdempotentCommand(store);

        assertThrows(IdempotencyPayloadMismatchException.class,
                () -> command.execute("emprestimos", "chave-1", Map.of("a", 2), String.class, () -> "novo"));
    }

    @Test
    void falhaDoEfeitoLiberaAChave() {
        RecordingStore store = new RecordingStore();
        IdempotentCommand command = new IdempotentCommand(store);

        assertThrows(IllegalStateException.class,
                () -> command.execute("emprestimos", "chave-1", Map.of("a", 1), String.class, () -> {
                    throw new IllegalStateException("recusou");
                }));

        assertEquals(1, store.abandons);
        assertEquals(0, store.completes);
    }

    private static final class RecordingStore implements IdempotencyStore {
        private int begins;
        private int completes;
        private int abandons;
        private IdempotencyDecision next = IdempotencyDecision.started();

        @Override
        public IdempotencyDecision begin(String scope, String key, String requestHash) {
            begins++;
            return next;
        }

        @Override
        public void complete(String scope, String key, String responseBody) {
            completes++;
        }

        @Override
        public void abandon(String scope, String key) {
            abandons++;
        }
    }
}
