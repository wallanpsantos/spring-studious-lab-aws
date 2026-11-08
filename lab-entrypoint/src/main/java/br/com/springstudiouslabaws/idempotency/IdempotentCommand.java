package br.com.springstudiouslabaws.idempotency;

import br.com.springstudiouslabaws.exceptions.IdempotencyConflictException;
import br.com.springstudiouslabaws.exceptions.IdempotencyPayloadMismatchException;
import br.com.springstudiouslabaws.exceptions.InvalidIdempotencyKeyException;
import br.com.springstudiouslabaws.utils.JsonUtil;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;
import java.util.regex.Pattern;

@Component
public class IdempotentCommand {

    private static final Pattern KEY = Pattern.compile("^[A-Za-z0-9._:-]{1,200}$");

    private final IdempotencyStore store;

    public IdempotentCommand(IdempotencyStore store) {
        this.store = store;
    }

    public <T> T execute(String scope, String key, Object request, Class<T> responseType, Supplier<T> action) {
        if (key == null || key.isBlank()) {
            return action.get();
        }
        if (!KEY.matcher(key).matches()) {
            throw new InvalidIdempotencyKeyException(
                    "Idempotency-Key deve ter de 1 a 200 caracteres: letras, números, ponto, underline, dois-pontos ou hífen");
        }
        String hash = RequestHash.sha256(JsonUtil.toJson(request));
        IdempotencyDecision decision = store.begin(scope, key, hash);
        return switch (decision.kind()) {
            case REPLAY -> JsonUtil.toObject(decision.responseBody(), responseType);
            case IN_PROGRESS -> throw new IdempotencyConflictException(
                    "Requisição com esta Idempotency-Key ainda está em andamento");
            case PAYLOAD_MISMATCH -> throw new IdempotencyPayloadMismatchException(
                    "Idempotency-Key reutilizada com outro corpo");
            case STARTED -> run(scope, key, action);
        };
    }

    private <T> T run(String scope, String key, Supplier<T> action) {
        T result;
        try {
            result = action.get();
        } catch (RuntimeException exception) {
            store.abandon(scope, key);
            throw exception;
        }
        store.complete(scope, key, JsonUtil.toJson(result));
        return result;
    }
}
