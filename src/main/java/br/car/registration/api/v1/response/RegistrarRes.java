package br.car.registration.api.v1.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "Registrar response")
public class RegistrarRes {

    @Schema(description = "The generated identifier of the registrar", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "The identifier of the registrar", example = "12345678901")
    private String identifier;

    @Schema(description = "The name of the registrar", example = "John Doe")
    private String name;

    @Schema(description = "The mother's name of the registrar", example = "Mary Doe")
    private String mothersName;

    @Schema(description = "The birth date of the registrar", example = "1990-01-01")
    private LocalDate dateOfBirth;

    @Schema(description = "The attributes of the registrar")
    private List<AttributeRes> attributes;
}
