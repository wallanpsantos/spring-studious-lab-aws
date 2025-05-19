package br.com.springstudiouslabaws.exceptions;

import br.com.springstudiouslabaws.exceptions.response.ErrorDetailsMessage;
import br.com.springstudiouslabaws.exceptions.response.ErrorMessage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ValidationExceptionHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final Logger log = LoggerFactory.getLogger(ValidationExceptionHandler.class);

    /// Quando um campo obrigatório está ausente ou inválido em um objeto JSON.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        // Coleta todos os erros de campo
        List<ErrorDetailsMessage> campos = fieldErrors.stream()
                .map(fieldError -> new ErrorDetailsMessage(fieldError.getField(), fieldError.getDefaultMessage())).collect(Collectors.toList());

        // Cria a resposta de erro no formato desejado
        var erroResponse = new ErrorMessage(
                HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação dos dados da requisição",
                "Um ou mais campos da requisição possuem valores inválidos ou estão ausentes",
                LocalDateTime.now().format(FORMATTER),
                campos
        );

        log.error("Erro de validação: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroResponse);
    }

    /// Para violações de restrições em campos específicos.
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /// Quando um parâmetro obrigatório está faltando.
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        List<ErrorDetailsMessage> campos = new ArrayList<>();

        var erroCampo = new ErrorDetailsMessage(parameterName, "O campo '" + parameterName + "' é obrigatório");
        campos.add(erroCampo);

        var erroResponse = new ErrorMessage(
                HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação dos dados da requisição",
                "Um ou mais campos da requisição possuem valores inválidos ou estão ausentes",
                LocalDateTime.now().format(FORMATTER),
                campos
        );

        log.error("Parâmetro obrigatório ausente: {}", parameterName);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroResponse);
    }


    /// Para erros de binding em formulários ou parâmetros de consulta.
    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(BindException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        // Coleta todos os erros de campo
        List<ErrorDetailsMessage> errorList = fieldErrors.stream().map(fieldError ->
                new ErrorDetailsMessage(fieldError.getField(), fieldError.getDefaultMessage())).collect(Collectors.toList());

        // Se não houver erros específicos de campo, verifica se há erros globais
        if (errorList.isEmpty() && !ex.getBindingResult().getGlobalErrors().isEmpty()) {
            ex.getBindingResult().getGlobalErrors().stream().map(objectError ->
                    new ErrorDetailsMessage(objectError.getObjectName(), objectError.getDefaultMessage())).forEachOrdered(errorList::add);
        }

        // Adiciona mensagem específica para parâmetro faltante se não houver outros erros
        if (ObjectUtils.isNotEmpty(ex.getFieldErrors())) {
            ex.getFieldErrors().stream().map(field ->
                    new ErrorDetailsMessage(field.getField(), "O campo '" + field.getField() + "' é obrigatório")).forEachOrdered(errorList::add);
        }

        // Cria a resposta de erro no formato desejado
        var erroResponse = new ErrorMessage(
                HttpStatus.BAD_REQUEST.name(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação dos dados da requisição",
                "Um ou mais campos da requisição possuem valores inválidos ou estão ausentes",
                LocalDateTime.now().format(FORMATTER),
                errorList
        );

        log.error("Erro de validação (Bind Exception): {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroResponse);
    }

}

