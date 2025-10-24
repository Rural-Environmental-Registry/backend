package br.car.registration.api.v1.request;

import java.util.List;

import org.geojson.GeoJsonObject;

import br.car.registration.enums.EntityTypesEnum;
import br.car.registration.validation.ValidAttributes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;

@Data
@Schema(description = "Subarea request")
public class SubAreaReq {

    @Schema(description = "The geometry of the subarea", requiredMode = Schema.RequiredMode.REQUIRED)
    private GeoJsonObject geometry;

    @Schema(description = "The area of the subarea", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double area;

    @Schema(description = "The type of the subarea", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String areaType;

    @Schema(description = "The attributes of the subarea")
    @ValidAttributes(entity = EntityTypesEnum.SUBAREA)
    private @Valid List<AttributeReq> attributes;
}
