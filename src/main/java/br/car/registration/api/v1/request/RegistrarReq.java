package br.car.registration.api.v1.request;

import br.car.registration.enums.EntityTypesEnum;
import br.car.registration.validation.ValidAttributes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "Registrar request")
public class RegistrarReq {

    @Schema(description = "The identifier of the registrar", example = "12345678901")
    private String identifier;

    @Schema(description = "The name of the registrar", example = "John Doe")
    private String name;

    @Schema(description = "The mother's name of the registrar", example = "Mary Doe")
    private String mothersName;

    @Schema(description = "The birth date of the registrar", example = "1990-01-01")
    private LocalDate dateOfBirth;

    @Schema(description = "The attributes of the registrar")
    @ValidAttributes(entity = EntityTypesEnum.REGISTRAR)
    private List<AttributeReq> attributes;
}
