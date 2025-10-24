package br.car.registration.api.v1.request;

import br.car.registration.enums.EntityTypesEnum;
import br.car.registration.validation.ValidAttributes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "Owner request")
public class OwnerReq {

    @Schema(description = "The identifier of the owner", example = "12345678901")
    private String identifier;

    @Schema(description = "The name of the owner", example = "John Doe")
    private String name;

    @Schema(description = "The mother's name of the owner", example = "Mary Doe")
    private String mothersName;

    @Schema(description = "The birth date of the owner", example = "1990-01-01")
    private LocalDate dateOfBirth;

    @Schema(description = "The attributes of the owners")
    @ValidAttributes(entity = EntityTypesEnum.OWNER)
    private List<AttributeReq> attributes;
}
