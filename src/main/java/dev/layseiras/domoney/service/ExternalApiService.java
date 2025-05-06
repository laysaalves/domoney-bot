package dev.layseiras.domoney.service;

import dev.layseiras.domoney.dto.ExternalApiDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ExternalApiService {

    private final WebClient webClient;

    public ExternalApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<ExternalApiDTO> fetch() {
        return webClient.get()
                .uri("/activity")
                .retrieve().bodyToFlux(ExternalApiDTO.class)
                .collectList()
                .block();
    }
}
