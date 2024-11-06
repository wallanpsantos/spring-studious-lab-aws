package br.com.springstudiouslabaws.labcore.exceptions;

public class PaymentNotFoundException extends RuntimeException {
//    Exception para regras de dominio, como validar e-mail, nome, cpf, etc

    public PaymentNotFoundException(String message) {
        super(message);
    }
}
