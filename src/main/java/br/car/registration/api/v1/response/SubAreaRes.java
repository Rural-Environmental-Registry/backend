package br.car.registration.api.v1.response;

import java.util.List;
import java.util.UUID;

import org.geojson.GeoJsonObject;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;

@Data
public class SubAreaRes {

    @Schema(description = "The subarea's unique identifier", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;

    @Schema(description = "The geometry of the subarea", requiredMode = Schema.RequiredMode.REQUIRED)
    private GeoJsonObject geometry;

    @Schema(description = "The area of the subarea", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double area;

    @Schema(description = "The type of the subarea", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String areaType;

    @Schema(description = "The subarea's attributes", requiredMode = Schema.RequiredMode.REQUIRED)
    private @Valid List<AttributeRes> attributes;
}
