package br.com.springstudiouslabaws.exceptions;

public class IdempotencyPayloadMismatchException extends RuntimeException {

    public IdempotencyPayloadMismatchException(String message) {
        super(message);
    }
}
