package br.car.registration.controller;

import br.car.registration.api.v1.CalculationApi;
import br.car.registration.api.v1.request.CalculationEngineReq;
import br.car.registration.service.CalculationService;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/calculations")
@AllArgsConstructor
class CalculationController implements CalculationApi {

    private final CalculationService service;

    @Override
    public ResponseEntity<Map<String, Object>> execute(@RequestBody CalculationEngineReq req) {
        return ResponseEntity.ok(service.clip(req));
    }
}