package br.com.springstudiouslabaws.exceptions;

public class LoanUpdateConflictException extends RuntimeException {

    public LoanUpdateConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
