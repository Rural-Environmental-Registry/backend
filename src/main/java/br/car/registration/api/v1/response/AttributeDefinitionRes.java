package br.car.registration.api.v1.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import br.car.registration.enums.AttributeTypesEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "The attribute definition of an attribute")
public class AttributeDefinitionRes {

    @Schema(description = "The UUID of the attribute definition", example = "a1b2c3d4", requiredMode = Schema.RequiredMode.REQUIRED)
    private String attributeDefinitionId;

    @Schema(description = "The type of the attribute", example = "STRING", requiredMode = Schema.RequiredMode.REQUIRED)
    private AttributeTypesEnum type;

    @Schema(description = "The name of the attribute", example = "firstName", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "The allowed values of the attribute", example = "[\"owner\", \"landholder\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<String> allowedValues;
}
