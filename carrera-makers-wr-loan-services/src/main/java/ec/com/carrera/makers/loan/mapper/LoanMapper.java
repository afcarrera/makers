package ec.com.carrera.makers.loan.mapper;

import ec.com.carrera.makers.loan.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LoanMapper {
    Loan mapToEntity(ec.com.carrera.makers.loan.dto.Loan domain);

    void updateEntity(ec.com.carrera.makers.loan.dto.Loan domain, @MappingTarget Loan existingDomain);
}
