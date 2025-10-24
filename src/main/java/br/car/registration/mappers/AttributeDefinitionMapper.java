package br.car.registration.mappers;

import br.car.registration.api.v1.request.AttributeDefinitionReq;
import br.car.registration.api.v1.response.AttributeDefinitionRes;
import br.car.registration.domain.attributes.AttributeDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component
public interface AttributeDefinitionMapper {

    @Mapping(target = "attributeDefinitionId", ignore = true)
    AttributeDefinition toEntity(AttributeDefinitionReq attributeDefinitionReq);

    AttributeDefinitionRes toResponse(AttributeDefinition attributeDefinition);
}
