package br.com.springstudiouslabaws.config.mongodb;

import br.com.springstudiouslabaws.entities.IdempotencyEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IdempotencyMongoRepository extends MongoRepository<IdempotencyEntity, String> {
}
