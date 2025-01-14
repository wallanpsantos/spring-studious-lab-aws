package br.com.springstudiouslabaws.labentrypoint.facade;

import br.com.springstudiouslabaws.labcore.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.labcore.usecases.loan.LoanUseCase;
import br.com.springstudiouslabaws.labentrypoint.dtos.request.LoanRequestDTO;
import br.com.springstudiouslabaws.labentrypoint.dtos.response.LoanResponseDTO;
import br.com.springstudiouslabaws.labentrypoint.mappers.DomainToDTOMapper;
import br.com.springstudiouslabaws.labentrypoint.mappers.EntryToCoreMapper;
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

    public LoanResponseDTO createLoan(LoanRequestDTO loanRequestDTO) {
        LoanDomain loanDomain = entryToCoreMapper.toDomain(loanRequestDTO);
        LoanDomain processedLoan = loanUseCase.processLoan(loanDomain);
        return domainToDTOMapper.toDTO(processedLoan);
    }

}
