package br.com.springstudiouslabaws.labdataprovider.mappers;

import br.com.springstudiouslabaws.labcore.domain.loan.LoanDomain;
import br.com.springstudiouslabaws.labdataprovider.entities.LoanEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface LoanCoreToDataMapper {

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
        // ignorar campos não mapeados e a não emitir avisos.
    LoanEntity toEntity(LoanDomain domain);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
        // ignorar campos não mapeados e a não emitir avisos.
    LoanDomain toDomain(LoanEntity entity);
}
