package br.car.registration.api.v1;

import br.car.registration.api.v1.request.CalculationEngineReq;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/v1/calculations")
public interface CalculationApi {

    @PostMapping(path = "/execute", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Map<String, Object>> execute(@RequestBody CalculationEngineReq req);
}
