package br.com.springstudiouslabaws.controllers;

import br.com.springstudiouslabaws.dtos.request.loan.LoanRequest;
import br.com.springstudiouslabaws.dtos.response.loan.LoanResponse;
import br.com.springstudiouslabaws.facade.LoanFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/emprestimos")
@Tag(name = "Empréstimos")
public class LoanController {

    private final LoanFacade loanFacade;

    public LoanController(LoanFacade loanFacade) {
        this.loanFacade = loanFacade;
    }

    @PostMapping
    @Operation(summary = "Cria um empréstimo", description = """
            Sem o cabeçalho Idempotency-Key, repetir a chamada cria outro empréstimo.
            Com o cabeçalho, a mesma chave e o mesmo corpo devolvem a primeira resposta.
            Outro corpo com a mesma chave responde 422. Uma chave ainda em processamento responde 409.
            A chave fica guardada enquanto o banco existir, o que cobre o prazo de retenção da fila.
            """)
    @ApiResponse(responseCode = "201", description = "Empréstimo criado, ou a mesma resposta da primeira tentativa")
    @ApiResponse(responseCode = "400", description = "JSON ilegível ou Idempotency-Key inválida")
    @ApiResponse(responseCode = "409", description = "A mesma chave ainda está em processamento")
    @ApiResponse(responseCode = "422", description = "Dados inválidos ou chave reutilizada com outro corpo")
    public ResponseEntity<LoanResponse> createLoan(
            @Parameter(description = "Identificador estável da intenção. O cliente gera uma vez e reenvia na retentativa.")
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @Valid @RequestBody LoanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanFacade.createLoan(idempotencyKey, request));
    }

    @GetMapping("/{loanId}")
    @Operation(summary = "Busca um empréstimo pelo identificador gerado na criação")
    @ApiResponse(responseCode = "200", description = "Empréstimo encontrado")
    @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")
    public ResponseEntity<LoanResponse> findLoan(@PathVariable String loanId) {
        return ResponseEntity.ok(loanFacade.findLoan(loanId));
    }
}
