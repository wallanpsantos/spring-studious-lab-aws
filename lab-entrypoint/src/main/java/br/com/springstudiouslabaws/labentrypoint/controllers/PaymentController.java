package br.com.springstudiouslabaws.labentrypoint.controllers;

import br.com.springstudiouslabaws.labentrypoint.dtos.request.payment.PaymentRequest;
import br.com.springstudiouslabaws.labentrypoint.dtos.response.payment.PaymentResponse;
import br.com.springstudiouslabaws.labentrypoint.facade.PaymentFacade;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/pagamentos")
public class PaymentController {

    private final PaymentFacade paymentFacade;

    public PaymentController(PaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentFacade.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
