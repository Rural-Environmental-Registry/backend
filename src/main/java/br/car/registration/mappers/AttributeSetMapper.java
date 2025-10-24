package br.car.registration.mappers;

import br.car.registration.api.v1.request.AttributeSetReq;
import br.car.registration.api.v1.response.AttributeSetRes;
import br.car.registration.domain.attributes.AttributeSet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface AttributeSetMapper {

    @Mapping(target = "attributeSetId", ignore = true)
    AttributeSet toEntity(AttributeSetReq attributeSetReq);

    AttributeSetRes toResponse(AttributeSet attributeSet);
}
