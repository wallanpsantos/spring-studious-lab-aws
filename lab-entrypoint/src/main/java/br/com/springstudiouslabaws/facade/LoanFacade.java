package br.com.springstudiouslabaws.facade;

import br.com.springstudiouslabaws.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.dtos.request.loan.LoanRequest;
import br.com.springstudiouslabaws.dtos.response.loan.LoanResponse;
import br.com.springstudiouslabaws.mappers.DomainToDTOMapper;
import br.com.springstudiouslabaws.mappers.EntryToCoreMapper;
import br.com.springstudiouslabaws.usecases.loan.LoanUseCase;
import org.springframework.stereotype.Component;

@Component
public class LoanFacade {

    private final LoanUseCase loanUseCase;
    private final EntryToCoreMapper entryToCoreMapper;
    private final DomainToDTOMapper domainToDTOMapper;

    public LoanFacade(LoanUseCase loanUseCase, EntryToCoreMapper entryToCoreMapper, DomainToDTOMapper domainToDTOMapper) {
        this.loanUseCase = loanUseCase;
        this.entryToCoreMapper = entryToCoreMapper;
        this.domainToDTOMapper = domainToDTOMapper;
    }

    public LoanResponse createLoan(LoanRequest loanRequest) {
        LoanDomain loanDomain = entryToCoreMapper.toDomain(loanRequest);
        LoanDomain processedLoan = loanUseCase.processLoan(loanDomain);
        return domainToDTOMapper.toDTO(processedLoan);
    }

}
