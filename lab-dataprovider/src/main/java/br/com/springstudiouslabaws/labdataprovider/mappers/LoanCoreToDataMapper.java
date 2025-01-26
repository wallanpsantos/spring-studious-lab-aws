package br.com.springstudiouslabaws.labdataprovider.mappers;

import br.com.springstudiouslabaws.labcore.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.labdataprovider.entities.LoanEntity;
import org.mapstruct.InheritInverseConfiguration;
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
    LoanEntity toEntity(LoanDomain domain);

    /// Mapeamento de volta (inverso) - Inverte automaticamente as configurações de mapeamento do toEntity
    //  Equivalente a: @Mapping(target = "clientId", source = "client.clientId")
    @InheritInverseConfiguration
    LoanDomain toDomain(LoanEntity entity);
}
