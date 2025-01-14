package br.com.springstudiouslabaws.labentrypoint.controllers;

import br.com.springstudiouslabaws.labentrypoint.dtos.request.PaymentRequestDTO;
import br.com.springstudiouslabaws.labentrypoint.dtos.response.PaymentResponseDTO;
import br.com.springstudiouslabaws.labentrypoint.facade.PaymentFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/pagamentos")
@Validated
public class PaymentController {

    private final PaymentFacade paymentFacade;

    public PaymentController(PaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO response = paymentFacade.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<PaymentResponseDTO> setPayment(@Valid @RequestBody PaymentRequestDTO request) {
        PaymentResponseDTO response = paymentFacade.processPayment(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
