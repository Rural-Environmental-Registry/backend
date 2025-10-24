package br.car.registration.api.v1.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Attribute request")
public class AttributeReq {

    @Schema(description = "The name of the attribute", example = "name")
    private String name;

    @Schema(description = "The value of the attribute", example = "John Doe")
    private String value;
}
