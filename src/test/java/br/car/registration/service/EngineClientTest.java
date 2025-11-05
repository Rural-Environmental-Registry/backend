package br.car.registration.service;

import br.car.registration.api.v1.request.CalculationEngineReq;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {
        "cardpg.engine.workflow=myWorkflow",
        "cardpg.engine.base-url=https://test.example.com/api",
        "cardpg.engine.timeout.connect-ms=3000",
        "cardpg.engine.timeout.read-ms=60000",
        "FRONTEND_URLS=http://localhost:3000",
        "HASH_PREFIX=TEST",
        "spring.flyway.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password="
})
public class EngineClientTest {

    @Autowired
    private EngineClient engineClient;

    @Test
    void engineClientBeanExistsWhenConfigured() {
        assertThat(engineClient).isNotNull();
    }

    @Test
    void execute_WhenValidRequest_ShouldHandleGracefully() {
        CalculationEngineReq request = createTestRequest();
        
        try {
            Map<String, Object> result = engineClient.execute(request);
            assertThat(result).isNotNull();
        } catch (Exception e) {
            assertThat(e.getMessage()).contains("Falha na comunicação com motor de cálculo");
        }
    }

    @Test
    void execute_WhenValidRequest_ShouldThrowExceptionDueToUnavailableService() {
        CalculationEngineReq request = createTestRequest();
        
        assertThatThrownBy(() -> engineClient.execute(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Falha na comunicação com motor de cálculo");
    }

    @Test
    void execute_WhenEmptyRequest_ShouldThrowExceptionDueToUnavailableService() {
        CalculationEngineReq emptyRequest = new CalculationEngineReq();
        
        assertThatThrownBy(() -> engineClient.execute(emptyRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Falha na comunicação com motor de cálculo");
    }

    private CalculationEngineReq createTestRequest() {
        CalculationEngineReq.GeoJson geoJson = new CalculationEngineReq.GeoJson();
        geoJson.setType("FeatureCollection");
        geoJson.setFeatures(java.util.List.of());

        CalculationEngineReq.Parameters parameters = new CalculationEngineReq.Parameters();
        parameters.setGeojson(geoJson);

        CalculationEngineReq request = new CalculationEngineReq();
        request.setParameters(parameters);
        return request;
    }
}
