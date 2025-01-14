package br.com.springstudiouslabaws.labentrypoint.controllers;

import br.com.springstudiouslabaws.labentrypoint.dtos.request.LoanRequestDTO;
import br.com.springstudiouslabaws.labentrypoint.dtos.response.LoanResponseDTO;
import br.com.springstudiouslabaws.labentrypoint.facade.LoanFacade;
import jakarta.validation.Valid;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@ReadingConverter
@RequestMapping("/v1/emprestimos")
public class LoanController {

    private final LoanFacade loanFacade;

    public LoanController(LoanFacade loanFacade) {
        this.loanFacade = loanFacade;
    }

    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@Valid @RequestBody LoanRequestDTO request) {
        LoanResponseDTO response = loanFacade.createLoan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
