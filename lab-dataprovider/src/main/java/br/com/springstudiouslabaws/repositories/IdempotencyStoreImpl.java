package br.com.springstudiouslabaws.repositories;

import br.com.springstudiouslabaws.config.mongodb.IdempotencyMongoRepository;
import br.com.springstudiouslabaws.entities.IdempotencyEntity;
import br.com.springstudiouslabaws.idempotency.IdempotencyDecision;
import br.com.springstudiouslabaws.idempotency.IdempotencyStore;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class IdempotencyStoreImpl implements IdempotencyStore {

    static final String IN_PROGRESS = "IN_PROGRESS";
    static final String SUCCEEDED = "SUCCEEDED";

    private final IdempotencyMongoRepository repository;

    public IdempotencyStoreImpl(IdempotencyMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public IdempotencyDecision begin(String scope, String key, String requestHash) {
        String id = id(scope, key);
        try {
            repository.insert(new IdempotencyEntity(id, requestHash, IN_PROGRESS, null, Instant.now()));
            return IdempotencyDecision.started();
        } catch (DuplicateKeyException exception) {
            IdempotencyEntity existing = repository.findById(id).orElseThrow(() -> exception);
            return IdempotencyDecision.fromExisting(
                    existing.requestHash(), existing.state(), existing.responseBody(), requestHash);
        }
    }

    @Override
    public void complete(String scope, String key, String responseBody) {
        String id = id(scope, key);
        IdempotencyEntity existing = repository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Chave de idempotência ausente: " + id));
        repository.save(new IdempotencyEntity(
                id, existing.requestHash(), SUCCEEDED, responseBody, existing.createdAt()));
    }

    @Override
    public void abandon(String scope, String key) {
        String id = id(scope, key);
        repository.findById(id).ifPresent(existing -> {
            if (IN_PROGRESS.equals(existing.state())) {
                repository.deleteById(id);
            }
        });
    }

    private static String id(String scope, String key) {
        return scope + ":" + key;
    }
}
