package br.car.registration.controller;

import br.car.registration.api.v1.request.CalculationEngineReq;
import br.car.registration.service.CalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculationControllerTest {

    @Mock
    private CalculationService calculationService;

    @InjectMocks
    private CalculationController calculationController;

    private CalculationEngineReq validRequest;
    private Map<String, Object> expectedResponse;

    @BeforeEach
    void setUp() {
        validRequest = createValidCalculationRequest();
        expectedResponse = createExpectedResponse();
    }

    @Test
    void execute_WhenValidRequest_ShouldReturnOkResponse() {
        // Given
        when(calculationService.clip(validRequest)).thenReturn(expectedResponse);

        // When
        ResponseEntity<Map<String, Object>> response = calculationController.execute(validRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(calculationService).clip(validRequest);
    }

    @Test
    void execute_WhenServiceReturnsEmptyMap_ShouldReturnOkResponse() {
        // Given
        Map<String, Object> emptyResponse = new HashMap<>();
        when(calculationService.clip(validRequest)).thenReturn(emptyResponse);

        // When
        ResponseEntity<Map<String, Object>> response = calculationController.execute(validRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
        verify(calculationService).clip(validRequest);
    }

    @Test
    void execute_WhenServiceReturnsNull_ShouldReturnOkResponse() {
        // Given
        when(calculationService.clip(validRequest)).thenReturn(null);

        // When
        ResponseEntity<Map<String, Object>> response = calculationController.execute(validRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNull();
        verify(calculationService).clip(validRequest);
    }

    @Test
    void execute_WhenServiceThrowsException_ShouldPropagateException() {
        // Given
        RuntimeException serviceException = new RuntimeException("Service error");
        when(calculationService.clip(validRequest)).thenThrow(serviceException);

        // When & Then
        try {
            calculationController.execute(validRequest);
        } catch (RuntimeException e) {
            assertThat(e).isEqualTo(serviceException);
        }
        verify(calculationService).clip(validRequest);
    }

    @Test
    void execute_WhenRequestIsNull_ShouldPassNullToService() {
        // Given
        when(calculationService.clip(null)).thenReturn(expectedResponse);

        // When
        ResponseEntity<Map<String, Object>> response = calculationController.execute(null);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(calculationService).clip(null);
    }

    @Test
    void execute_WhenRequestHasEmptyParameters_ShouldPassToService() {
        // Given
        CalculationEngineReq emptyRequest = new CalculationEngineReq();
        when(calculationService.clip(emptyRequest)).thenReturn(expectedResponse);

        // When
        ResponseEntity<Map<String, Object>> response = calculationController.execute(emptyRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedResponse);
        verify(calculationService).clip(emptyRequest);
    }

    @Test
    void execute_WhenRequestHasComplexData_ShouldPassToService() {
        // Given
        CalculationEngineReq complexRequest = createComplexCalculationRequest();
        Map<String, Object> complexResponse = createComplexResponse();
        when(calculationService.clip(complexRequest)).thenReturn(complexResponse);

        // When
        ResponseEntity<Map<String, Object>> response = calculationController.execute(complexRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(complexResponse);
        verify(calculationService).clip(complexRequest);
    }

    @Test
    void execute_WhenServiceReturnsLargeResponse_ShouldReturnOkResponse() {
        // Given
        Map<String, Object> largeResponse = createLargeResponse();
        when(calculationService.clip(validRequest)).thenReturn(largeResponse);

        // When
        ResponseEntity<Map<String, Object>> response = calculationController.execute(validRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(largeResponse);
        assertThat(response.getBody().size()).isGreaterThan(10);
        verify(calculationService).clip(validRequest);
    }

    private CalculationEngineReq createValidCalculationRequest() {
        CalculationEngineReq.GeoJson geoJson = new CalculationEngineReq.GeoJson();
        geoJson.setType("FeatureCollection");
        geoJson.setFeatures(List.of());

        CalculationEngineReq.Parameters parameters = new CalculationEngineReq.Parameters();
        parameters.setGeojson(geoJson);

        CalculationEngineReq request = new CalculationEngineReq();
        request.setParameters(parameters);
        return request;
    }

    private CalculationEngineReq createComplexCalculationRequest() {
        CalculationEngineReq.Feature feature = new CalculationEngineReq.Feature();
        feature.setType("Feature");

        CalculationEngineReq.Geometry geometry = new CalculationEngineReq.Geometry();
        geometry.setType("Polygon");
        geometry.setCoordinates(List.of(List.of(List.of(0.0, 0.0), List.of(1.0, 0.0), List.of(1.0, 1.0), List.of(0.0, 1.0), List.of(0.0, 0.0))));
        feature.setGeometry(geometry);

        CalculationEngineReq.Properties properties = new CalculationEngineReq.Properties();
        properties.setTipo("PROPERTY_HEADQUARTERS");
        properties.setLayerCode("PROPERTY_HEADQUARTERS");
        feature.setProperties(properties);

        CalculationEngineReq.GeoJson geoJson = new CalculationEngineReq.GeoJson();
        geoJson.setType("FeatureCollection");
        geoJson.setFeatures(List.of(feature));

        CalculationEngineReq.Parameters parameters = new CalculationEngineReq.Parameters();
        parameters.setGeojson(geoJson);

        CalculationEngineReq request = new CalculationEngineReq();
        request.setParameters(parameters);
        return request;
    }

    private Map<String, Object> createExpectedResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("result", "calculation completed");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    private Map<String, Object> createComplexResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("calculationId", "calc-12345");
        response.put("results", Map.of(
                "area", 100.5,
                "perimeter", 40.2,
                "coordinates", List.of(0.0, 0.0, 1.0, 1.0)
        ));
        response.put("metadata", Map.of(
                "processingTime", 150,
                "version", "1.0.0"
        ));
        return response;
    }

    private Map<String, Object> createLargeResponse() {
        Map<String, Object> response = new HashMap<>();
        for (int i = 0; i < 20; i++) {
            response.put("key" + i, "value" + i);
        }
        response.put("status", "success");
        response.put("totalItems", 20);
        return response;
    }
}
