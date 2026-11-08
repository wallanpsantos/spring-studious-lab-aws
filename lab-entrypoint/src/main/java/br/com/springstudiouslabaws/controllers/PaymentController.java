package br.com.springstudiouslabaws.controllers;

import br.com.springstudiouslabaws.dtos.request.payment.PaymentRequest;
import br.com.springstudiouslabaws.dtos.response.payment.PaymentResponse;
import br.com.springstudiouslabaws.facade.PaymentFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/pagamentos")
@Tag(name = "Pagamentos")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    public PaymentController(PaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
    }

    @PostMapping
    @Operation(summary = "Registra pagamentos", description = """
            Sem o cabeçalho Idempotency-Key, repetir a chamada grava outro pagamento.
            Com o cabeçalho, a mesma chave e o mesmo corpo devolvem a primeira resposta.
            Outro corpo com a mesma chave responde 422. Uma chave ainda em processamento responde 409.
            """)
    @ApiResponse(responseCode = "201", description = "Pagamento registrado, ou a mesma resposta da primeira tentativa")
    @ApiResponse(responseCode = "400", description = "JSON ilegível ou Idempotency-Key inválida")
    @ApiResponse(responseCode = "409", description = "A mesma chave ainda está em processamento")
    @ApiResponse(responseCode = "422", description = "Dados inválidos ou chave reutilizada com outro corpo")
    public ResponseEntity<PaymentResponse> createPayment(
            @Parameter(description = "Identificador estável da intenção. O cliente gera uma vez e reenvia na retentativa.")
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentFacade.createPayment(idempotencyKey, request));
    }
}
