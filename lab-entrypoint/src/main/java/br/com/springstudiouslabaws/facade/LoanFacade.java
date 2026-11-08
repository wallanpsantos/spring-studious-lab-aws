package br.com.springstudiouslabaws.facade;

import br.com.springstudiouslabaws.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.dtos.request.loan.LoanRequest;
import br.com.springstudiouslabaws.dtos.response.loan.LoanResponse;
import br.com.springstudiouslabaws.idempotency.IdempotentCommand;
import br.com.springstudiouslabaws.mappers.DomainToDTOMapper;
import br.com.springstudiouslabaws.mappers.EntryToCoreMapper;
import br.com.springstudiouslabaws.usecases.loan.LoanUseCase;
import org.springframework.stereotype.Component;

@Component
public class LoanFacade {

    static final String SCOPE = "emprestimos";

    private final LoanUseCase loanUseCase;
    private final EntryToCoreMapper entryToCoreMapper;
    private final DomainToDTOMapper domainToDTOMapper;
    private final IdempotentCommand idempotentCommand;

    public LoanFacade(LoanUseCase loanUseCase,
                      EntryToCoreMapper entryToCoreMapper,
                      DomainToDTOMapper domainToDTOMapper,
                      IdempotentCommand idempotentCommand) {
        this.loanUseCase = loanUseCase;
        this.entryToCoreMapper = entryToCoreMapper;
        this.domainToDTOMapper = domainToDTOMapper;
        this.idempotentCommand = idempotentCommand;
    }

    public LoanResponse createLoan(String idempotencyKey, LoanRequest loanRequest) {
        return idempotentCommand.execute(SCOPE, idempotencyKey, loanRequest, LoanResponse.class, () -> {
            LoanDomain loanDomain = entryToCoreMapper.toDomain(loanRequest);
            return domainToDTOMapper.toDTO(loanUseCase.processLoan(loanDomain));
        });
    }

    public LoanResponse findLoan(String loanId) {
        return domainToDTOMapper.toDTO(loanUseCase.findByLoanId(loanId));
    }
}
