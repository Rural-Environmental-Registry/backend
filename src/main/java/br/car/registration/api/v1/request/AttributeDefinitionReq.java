package br.car.registration.api.v1.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

import br.car.registration.enums.AttributeTypesEnum;

@Data
@Schema(description = "The attribute definition of an attribute")
public class AttributeDefinitionReq {

    @Schema(description = "The type of the attribute", example = "STRING", requiredMode = Schema.RequiredMode.REQUIRED)
    private AttributeTypesEnum type;

    @Schema(description = "The name of the attribute", example = "firstName", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "The allowed values of the attribute", example = "[\"owner\", \"landholder\"]", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<String> allowedValues;
}
