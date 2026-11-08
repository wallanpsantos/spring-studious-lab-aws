package br.com.springstudiouslabaws.idempotency;

public interface IdempotencyStore {

    /**
     * Reserva a chave numa única escrita. A unicidade do identificador é o que impede duas tentativas de passarem juntas.
     */
    IdempotencyDecision begin(String scope, String key, String requestHash);

    void complete(String scope, String key, String responseBody);

    /**
     * Libera a chave só quando o efeito não aconteceu. Uma chave que ficou em andamento continua bloqueando novas tentativas.
     */
    void abandon(String scope, String key);
}
