package dev.layseiras.domoney.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GeminiApi {

    @Value("${api.gemini.key}")
    private String apiKey;

    @Value("${api.gemini.url}")
    private String apiUrl;

    public GeminiApi() {
    }

    public GeminiApi(String apiKey, String apiUrl) {
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }
}
