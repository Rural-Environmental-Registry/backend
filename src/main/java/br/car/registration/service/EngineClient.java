package br.car.registration.service;

import br.car.registration.api.v1.request.CalculationEngineReq;
import java.time.Duration;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class EngineClient {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_TYPE =
            new ParameterizedTypeReference<>() {};

    private final WebClient webClient;
    private final String workflow;

    EngineClient(WebClient engineWebClient,
                 @Value("${cardpg.engine.workflow}") String workflow) {
        this.webClient = engineWebClient;
        this.workflow = workflow;
    }

    public Map<String, Object> execute(CalculationEngineReq body) {
        var url = "/workflows/{workflow}/execute";
        return webClient.post()
                .uri(url, workflow)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(MAP_TYPE)
                .timeout(Duration.ofSeconds(60))
                .map(this::processarResposta)
                .onErrorResume(this::tratarErro)
                .block();
    }

    private Map<String, Object> processarResposta(Map<String, Object> resposta) {
        log.info("Resposta recebida do motor de cálculo: {}", resposta);
        return resposta;
    }

    private Mono<Map<String, Object>> tratarErro(Throwable erro) {
        log.error("Erro ao chamar motor de cálculo", erro);
        return Mono.error(new RuntimeException("Falha na comunicação com motor de cálculo", erro));
    }
}