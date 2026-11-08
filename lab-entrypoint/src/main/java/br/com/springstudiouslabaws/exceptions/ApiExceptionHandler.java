package br.com.springstudiouslabaws.exceptions;

import br.com.springstudiouslabaws.exceptions.ApiErrorResponse.FieldDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Class<?> target = ex.getBindingResult().getTarget() == null
                ? null
                : ex.getBindingResult().getTarget().getClass();
        List<FieldDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldDetail(
                        ContractFieldNames.toJsonPath(target, error.getField()),
                        error.getDefaultMessage()))
                .toList();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(ApiErrorResponse.of("VALIDATION_ERROR", "Dados da requisição inválidos", details));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            org.springframework.http.converter.HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.debug("JSON ilegível", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.of("MALFORMED_REQUEST", "O corpo da requisição não é um JSON válido", List.of()));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ApiErrorResponse error = switch (statusCode.value()) {
            case 400 -> ApiErrorResponse.of("MALFORMED_REQUEST", "A requisição é inválida", List.of());
            case 404 -> ApiErrorResponse.of("NOT_FOUND", "Recurso não encontrado", List.of());
            case 405 -> ApiErrorResponse.of("METHOD_NOT_ALLOWED", "Método não permitido", List.of());
            case 415 -> ApiErrorResponse.of("UNSUPPORTED_MEDIA_TYPE", "Content-Type não suportado", List.of());
            default -> ApiErrorResponse.of("REQUEST_ERROR", "A requisição não pôde ser processada", List.of());
        };
        return new ResponseEntity<>(error, headers, statusCode);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorResponse> business(BusinessException exception) {
        List<FieldDetail> details = exception.getErrors().stream()
                .map(message -> new FieldDetail(null, message))
                .toList();
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(ApiErrorResponse.of("BUSINESS_RULE", exception.getMessage(), details));
    }

    @ExceptionHandler({LoanNotFoundException.class, PaymentNotFoundException.class, ResourceNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> notFound(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.of("NOT_FOUND", exception.getMessage(), List.of()));
    }

    @ExceptionHandler(IdempotencyConflictException.class)
    public ResponseEntity<ApiErrorResponse> idempotencyConflict(IdempotencyConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiErrorResponse.of("IDEMPOTENCY_IN_PROGRESS", exception.getMessage(), List.of()));
    }

    @ExceptionHandler(IdempotencyPayloadMismatchException.class)
    public ResponseEntity<ApiErrorResponse> idempotencyPayload(IdempotencyPayloadMismatchException exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(ApiErrorResponse.of("IDEMPOTENCY_PAYLOAD_MISMATCH", exception.getMessage(), List.of()));
    }

    @ExceptionHandler(InvalidIdempotencyKeyException.class)
    public ResponseEntity<ApiErrorResponse> invalidKey(InvalidIdempotencyKeyException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.of("INVALID_IDEMPOTENCY_KEY", exception.getMessage(), List.of()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> unexpected(Exception exception) {
        log.error("Falha não tratada", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiErrorResponse.of("INTERNAL_ERROR", "Erro interno", List.of()));
    }
}
