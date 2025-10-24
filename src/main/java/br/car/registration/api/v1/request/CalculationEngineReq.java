package br.car.registration.api.v1.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Calculation Engine Request")
public class CalculationEngineReq {

    private Parameters parameters;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Parameters wrapper", requiredMode = Schema.RequiredMode.REQUIRED)
    public static class Parameters {

        private GeoJson geojson;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "The geojson interface", requiredMode = Schema.RequiredMode.REQUIRED)
    public static class GeoJson {

        @Schema(description = "GeoJSON type (always FeatureCollection)", example = "FeatureCollection", requiredMode = Schema.RequiredMode.REQUIRED)
        private String type;

        @Schema(description = "List of GeoJSON features in the collection", requiredMode = Schema.RequiredMode.REQUIRED)
        private List<Feature> features;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "The representation of a feature")
    public static class Feature {

        @Schema(description = "The type of the feature", example = "Feature", requiredMode = Schema.RequiredMode.REQUIRED)
        private String type;

        @Schema(description = "The geometry of the feature", requiredMode = Schema.RequiredMode.REQUIRED)
        private Geometry geometry;

        @Schema(description = "The properties of the feature", requiredMode = Schema.RequiredMode.REQUIRED)
        private Properties properties;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Geometry {

        @Schema(description = "The type of the geometry", example = "Polygon", requiredMode = Schema.RequiredMode.REQUIRED)
        private String type;

        @Schema(description = "The coordinates of the geometry", requiredMode = Schema.RequiredMode.REQUIRED)
        private Object coordinates;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "The properties of the feature required by the calculation engine", requiredMode = Schema.RequiredMode.REQUIRED)
    public static class Properties {
        @Schema(description = "The name of the layer", example = "PROPERTY_HEADQUARTERS", requiredMode = Schema.RequiredMode.REQUIRED)
        private String tipo;

        @Schema(description = "The alternative name of the layer", example = "PROPERTY_HEADQUARTERS", requiredMode = Schema.RequiredMode.REQUIRED)
        private String layerCode;
    }
}