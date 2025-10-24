package br.car.registration.api.v1.request;

import java.util.List;

import br.car.registration.enums.DocumentTypesEnum;
import br.car.registration.enums.EntityTypesEnum;
import br.car.registration.validation.ValidAttributes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;

@Data
@Schema(description = "Property document request")
public class PropertyDocumentReq {

    @Schema(description = "The property name on property documents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String registeredPropertyName;

    @Schema(description = "The area on property documents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double area;

    @Schema(description = "The type of property documents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DocumentTypesEnum documentType;

    @Schema(description = "The property documents attributes", requiredMode = Schema.RequiredMode.REQUIRED)
    @ValidAttributes(entity = EntityTypesEnum.PROPERTY_DOCUMENT)
    private @Valid List<AttributeReq> attributes;
}
