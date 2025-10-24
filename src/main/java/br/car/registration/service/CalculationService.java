package br.car.registration.service;

import br.car.registration.api.v1.request.CalculationEngineReq;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    private final EngineClient engineClient;


    CalculationService(EngineClient engineClient) {
        this.engineClient = engineClient;
    }

    public Map<String, Object> clip(CalculationEngineReq req) {
        return engineClient.execute(req);
    }
}