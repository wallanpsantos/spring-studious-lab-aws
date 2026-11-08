package br.com.springstudiouslabaws.exceptions;

public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException(String message) {
        super(message);
    }
}
