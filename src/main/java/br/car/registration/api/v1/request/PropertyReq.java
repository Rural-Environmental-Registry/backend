package br.car.registration.api.v1.request;

import java.time.Instant;
import java.util.List;

import org.geojson.GeoJsonObject;
import org.springframework.data.annotation.Transient;
import org.springframework.web.multipart.MultipartFile;

import br.car.registration.enums.EntityTypesEnum;
import br.car.registration.enums.LocationZonesEnum;
import br.car.registration.validation.ValidAttributes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Schema(description = "The property's request body")
public class PropertyReq {

    @Transient
    private static final EntityTypesEnum entityType = EntityTypesEnum.PROPERTY;

    @Schema(description = "The property's geometry, area, latitude ang longitude", requiredMode = Schema.RequiredMode.REQUIRED)
    private MainArea mainArea;

    @Schema(description = "The property's name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String propertyName;

    @Schema(description = "The property's State/District", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String stateDistrict;

    @Schema(description = "The property's Minicipality", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String municipality;

    @Schema(description = "The property's Zipcode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String zipcode;

    @Schema(description = "The property's Last Update Instant", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Instant updatedAt;

    @Schema(description = "The property's Location Zone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocationZonesEnum locationZone;

    @Schema(description = "The property's Image", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private MultipartFile mapImage;

    // TODO: Review this field
    // @NotEmpty(message = "The property must have at least one subarea")
    @Schema(description = "The property's subareas", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private @Valid List<SubAreaReq> subAreas;

    @NotEmpty(message = "The property must have at least one owner")
    @Schema(description = "The property's owners", requiredMode = Schema.RequiredMode.REQUIRED)
    private @Valid List<OwnerReq> owners;

    @Schema(description = "The registrar of the property", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private @Valid RegistrarReq registrar;

    @Schema(description = "The representative of the property", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private @Valid RepresentativeReq representative;

    @Schema(description = "The property's attributes", requiredMode = Schema.RequiredMode.REQUIRED)
    @ValidAttributes(entity = EntityTypesEnum.PROPERTY)
    private List<@Valid AttributeReq> attributes;

    @Schema(description = "The property's documents", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "The property must have at least one document")
    private List<@Valid PropertyDocumentReq> documents;

    public MultipartFile getMapImage() {
        return mapImage;
    }

    public void setMapImage(MultipartFile mapImage) {
        this.mapImage = mapImage;
    }

    @Getter
    @Setter
    public static class MainArea {
        private GeoJsonObject geometry;
        private Double area;
        private Double lat;
        private Double lon;
    }
}
