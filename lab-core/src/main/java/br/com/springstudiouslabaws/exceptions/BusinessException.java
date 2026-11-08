package br.com.springstudiouslabaws.exceptions;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exceção para representar erros de regras de negócio na aplicação.
 * Esta exceção pode conter uma mensagem única ou uma lista de erros de validação.
 */
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<String> errors;

    /**
     * Construtor para uma única mensagem de erro
     *
     * @param message Mensagem descrevendo o erro de negócio
     */
    public BusinessException(String message) {
        super(message);
        this.errors = Collections.singletonList(message);
    }

    /**
     * Construtor para uma lista de erros
     *
     * @param errors Lista de mensagens de erro
     */
    public BusinessException(List<String> errors) {
        super(String.join("; ", errors));
        this.errors = new ArrayList<>(errors);
    }

    /**
     * Construtor para mensagem de erro com causa
     *
     * @param message Mensagem descrevendo o erro de negócio
     * @param cause   Causa raiz da exceção
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errors = Collections.singletonList(message);
    }

    /**
     * Retorna a lista de erros
     *
     * @return Lista imutável de mensagens de erro
     */
    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    /**
     * Verifica se há múltiplos erros
     *
     * @return true se houver mais de um erro, false caso contrário
     */
    public boolean hasMultipleErrors() {
        return errors.size() > 1;
    }
}