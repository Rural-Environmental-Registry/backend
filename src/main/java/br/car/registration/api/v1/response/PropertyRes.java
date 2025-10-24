package br.car.registration.api.v1.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.geojson.GeoJsonObject;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.car.registration.enums.EntityTypesEnum;
import br.car.registration.enums.LocationZonesEnum;
import br.car.registration.validation.ValidAttributes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyRes {

    @Transient
    private static final EntityTypesEnum entityType = EntityTypesEnum.PROPERTY;

    @Schema(description = "The property's unique identifier", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID id;

    @Schema(description = "The property's geometry, area, latitude and longitude", requiredMode = Schema.RequiredMode.REQUIRED)
    private MainArea mainArea;
    // private GeoJsonObject geometry;

    @Schema(description = "The property's name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String propertyName;

    @Schema(description = "The property's State/District", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String stateDistrict;

    @Schema(description = "The property's Minicipality", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String municipality;

    @Schema(description = "The property's Zipcode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String zipcode;

    @Schema(description = "The property's Location Zone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocationZonesEnum locationZone;

    @Schema(description = "The property's Creation Instant", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Instant createdAt;

    @Schema(description = "The property's Last Update Instant", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Instant updatedAt;

    @Schema(description = "The property's Code", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String code;

    @Schema(description = "The property's subareas", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private @Valid List<SubAreaRes> subAreas;

    @Schema(description = "The property's owners", requiredMode = Schema.RequiredMode.REQUIRED)
    private @Valid List<OwnerRes> owners;

    @Schema(description = "The registrar of the property", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private @Valid RegistrarRes registrar;

    @Schema(description = "The representative of the property", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private @Valid RepresentativeRes representative;

    @ValidAttributes(entity = EntityTypesEnum.PROPERTY)
    private List<@Valid AttributeRes> attributes;

    @Schema(description = "The property's documents", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<@Valid PropertyDocumentRes> documents;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MainArea {
        private GeoJsonObject geometry;
        private Double area;
        private Double lat;
        private Double lon;
    }
}
