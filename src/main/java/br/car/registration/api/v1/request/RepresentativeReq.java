package br.car.registration.api.v1.request;

import br.car.registration.enums.EntityTypesEnum;
import br.car.registration.validation.ValidAttributes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "Representative request")
public class RepresentativeReq {

    @Schema(description = "The identifier of the representative", example = "12345678901")
    private String identifier;

    @Schema(description = "The name of the representative", example = "John Doe")
    private String name;

    @Schema(description = "The mother's name of the representative", example = "Mary Doe")
    private String mothersName;

    @Schema(description = "The birth date of the representative", example = "1990-01-01")
    private LocalDate dateOfBirth;

    @Schema(description = "The attributes of the representative")
    @ValidAttributes(entity = EntityTypesEnum.REPRESENTATIVE)
    private List<@Valid AttributeReq> attributes;
}
