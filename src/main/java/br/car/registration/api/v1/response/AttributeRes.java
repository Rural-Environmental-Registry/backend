package br.car.registration.api.v1.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Attribute response")
public class AttributeRes {

    @Schema(description = "The generated identifier of the attribute", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID attributeId;

    @Schema(description = "The name of the attribute", example = "name")
    private String name;

    @Schema(description = "The value of the attribute", example = "John Doe")
    private String value;
}
