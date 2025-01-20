package br.com.springstudiouslabaws.labdataprovider.mappers;

import br.com.springstudiouslabaws.labcore.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.labdataprovider.entities.LoanEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", // Define que o mapper será gerenciado pelo Spring Framework
        unmappedTargetPolicy = ReportingPolicy.IGNORE, // ignorar campos não mapeados e a não emitir avisos.
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) // não mapear nulo do objeto fonte
public interface LoanCoreToDataMapper {

    @Mapping(target = "client.clientId", source = "clientId")
    @Mapping(target = "client.clientName", source = "name")
    @Mapping(target = "client.clientEmail", source = "email")
    @Mapping(target = "lastUpdate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "loanId", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "status", constant = "PENDING")
    LoanEntity toEntity(LoanDomain domain);

    LoanDomain toDomain(LoanEntity entity);
}
