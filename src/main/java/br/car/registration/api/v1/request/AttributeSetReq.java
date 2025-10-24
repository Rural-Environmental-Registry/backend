package br.car.registration.api.v1.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

import br.car.registration.enums.EntityTypesEnum;

@Data
@Schema(description = "The attribute set of an entity type")
public class AttributeSetReq {

    @Schema(description = "The entity type of the attribute set", example = "OWNER", requiredMode = Schema.RequiredMode.REQUIRED)
    private EntityTypesEnum entityType;

    @Schema(description = "The list of attributes in the attribute set", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<AttributeDefinitionReq> attributes;
}
