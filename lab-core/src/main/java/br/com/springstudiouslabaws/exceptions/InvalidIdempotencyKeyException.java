package br.com.springstudiouslabaws.exceptions;

public class InvalidIdempotencyKeyException extends RuntimeException {

    public InvalidIdempotencyKeyException(String message) {
        super(message);
    }
}
