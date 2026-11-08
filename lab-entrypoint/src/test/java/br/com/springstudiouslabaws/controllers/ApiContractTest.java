package br.com.springstudiouslabaws.controllers;

import br.com.springstudiouslabaws.exceptions.ApiExceptionHandler;
import br.com.springstudiouslabaws.exceptions.IdempotencyConflictException;
import br.com.springstudiouslabaws.exceptions.IdempotencyPayloadMismatchException;
import br.com.springstudiouslabaws.exceptions.LoanNotFoundException;
import br.com.springstudiouslabaws.facade.LoanFacade;
import br.com.springstudiouslabaws.facade.PaymentFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {LoanController.class, PaymentController.class})
@Import(ApiExceptionHandler.class)
class ApiContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanFacade loanFacade;

    @MockitoBean
    private PaymentFacade paymentFacade;

    @Test
    void emprestimoInvalidoUsaOEnvelopeUnicoComONomeDoJson() throws Exception {
        mockMvc.perform(post("/v1/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "identificador_cliente": "123",
                                  "nome": "Jo",
                                  "email": "nao-e-email",
                                  "descricao_emprestimo": "Pessoal",
                                  "valor_solicitado": 10.00,
                                  "data_final_vencimento": "2027-12-29"
                                }
                                """))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.error.details[?(@.field == 'nome')]").exists())
                .andExpect(jsonPath("$.error.details[?(@.field == 'valor_solicitado')]").exists())
                .andExpect(jsonPath("$.error.details[?(@.field == 'email')]").exists());
    }

    @Test
    void jsonIlegivelResponde400() throws Exception {
        mockMvc.perform(post("/v1/emprestimos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("MALFORMED_REQUEST"));
    }

    @Test
    void criacaoEncaminhaAChaveDeIdempotencia() throws Exception {
        when(loanFacade.createLoan(eq("chave-123"), any())).thenReturn(null);

        mockMvc.perform(post("/v1/emprestimos")
                        .header("Idempotency-Key", "chave-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emprestimoValido()))
                .andExpect(status().isCreated());

        verify(loanFacade).createLoan(eq("chave-123"), any());
    }

    @Test
    void emprestimoAusenteResponde404() throws Exception {
        when(loanFacade.findLoan("ausente")).thenThrow(new LoanNotFoundException("Empréstimo não encontrado"));

        mockMvc.perform(get("/v1/emprestimos/ausente"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("NOT_FOUND"));
    }

    @Test
    void pagamentoSemItensResponde422() throws Exception {
        mockMvc.perform(post("/v1/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "identificador_cliente": "123", "pagamentos": [] }
                                """))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.error.details[?(@.field == 'pagamentos')]").exists());

        verify(paymentFacade, org.mockito.Mockito.never()).createPayment(isNull(), any());
    }

    @Test
    void pagamentoComItemInvalidoUsaOCaminhoJsonAninhado() throws Exception {
        mockMvc.perform(post("/v1/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "identificador_cliente": "123",
                                  "pagamentos": [
                                    { "identificador_emprestimo": " ", "valor_pagamento": 10.00 }
                                  ]
                                }
                                """))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.error.details[?(@.field == 'pagamentos[0].identificador_emprestimo')]").exists());

        verify(paymentFacade, org.mockito.Mockito.never()).createPayment(isNull(), any());
    }

    @Test
    void chaveEmAndamentoResponde409NoEnvelopeUnico() throws Exception {
        when(loanFacade.createLoan(eq("chave-123"), any()))
                .thenThrow(new IdempotencyConflictException(
                        "Requisição com esta Idempotency-Key ainda está em andamento"));

        mockMvc.perform(post("/v1/emprestimos")
                        .header("Idempotency-Key", "chave-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emprestimoValido()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error.code").value("IDEMPOTENCY_IN_PROGRESS"))
                .andExpect(jsonPath("$.error.message").value(
                        "Requisição com esta Idempotency-Key ainda está em andamento"));
    }

    @Test
    void mesmaChaveComOutroCorpoResponde422NoEnvelopeUnico() throws Exception {
        when(loanFacade.createLoan(eq("chave-123"), any()))
                .thenThrow(new IdempotencyPayloadMismatchException(
                        "Idempotency-Key reutilizada com outro corpo"));

        mockMvc.perform(post("/v1/emprestimos")
                        .header("Idempotency-Key", "chave-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emprestimoValido()))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.error.code").value("IDEMPOTENCY_PAYLOAD_MISMATCH"))
                .andExpect(jsonPath("$.error.message").value("Idempotency-Key reutilizada com outro corpo"));
    }

    private static String emprestimoValido() {
        return """
                {
                  "identificador_cliente": "123",
                  "nome": "João Silva",
                  "email": "joao@email.com",
                  "descricao_emprestimo": "Empréstimo pessoal",
                  "valor_solicitado": 5000.00,
                  "data_final_vencimento": "2027-12-29"
                }
                """;
    }
}
