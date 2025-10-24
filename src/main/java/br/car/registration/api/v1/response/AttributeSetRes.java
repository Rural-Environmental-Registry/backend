package br.car.registration.api.v1.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import br.car.registration.enums.EntityTypesEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "The attribute set of an entity type")
public class AttributeSetRes {

    @Schema(description = "The UUID of the attribute set", example = "1fb789f5-93f1-424f-9b55-af81d3ea741d", requiredMode = Schema.RequiredMode.REQUIRED)
    private String attributeSetId;

    @Schema(description = "The entity type of the attribute set", example = "OWNER", requiredMode = Schema.RequiredMode.REQUIRED)
    private EntityTypesEnum entityType;

    @Schema(description = "The list of attributes in the attribute set", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<AttributeDefinitionRes> attributes;
}
