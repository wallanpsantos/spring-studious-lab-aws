package br.com.springstudiouslabaws.labentrypoint.exceptions;

import br.com.springstudiouslabaws.labcore.exceptions.PaymentNotFoundException;
import br.com.springstudiouslabaws.labdataprovider.exceptions.ApiErrorMessage;
import br.com.springstudiouslabaws.labdataprovider.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class) // Simples
    public ResponseEntity<ApiErrorMessage> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return getApiErrorMessageResponseEntity(request, ex.getMessage(), ex);
    }

    @ExceptionHandler(PaymentNotFoundException.class) // Simples
    public ResponseEntity<ApiErrorMessage> paymentNotFoundException(PaymentNotFoundException ex, WebRequest request) {
        return getApiErrorMessageResponseEntity(request, ex.getMessage(), ex);
    }

    private ResponseEntity<ApiErrorMessage> getApiErrorMessageResponseEntity(WebRequest request, String message, Exception ex) {
        final var error = new ApiErrorMessage();
        error.setTitle("Recurso não encontrado");
        error.setMessage(message);
        error.setDescription(request.getDescription(false));
        error.setStatusCode(HttpStatus.NOT_FOUND.value());
        error.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


//    @ExceptionHandler(value = ElementNotExistsException.class) //Com classe genérica para encapsular um objeto de erro
//    protected ResponseEntity<Object> elementNotExistsException(ElementNotExistsException ex, WebRequest request) {
//
//        final var errorMessage = new ApiErrorMessage();
//        errorMessage.setTitle("Element Not Exists");
//        errorMessage.setDescription(ex.getMessage());
//        errorMessage.setStatusCode(NOT_FOUND.value());
//        errorMessage.setTimestamp(LocalDateTime.now());
//
//        return handleExceptionInternal(ex, new ApiErrorMessageWrapper<>(errorMessage), new HttpHeaders(), NOT_FOUND, request);
//    }
}

