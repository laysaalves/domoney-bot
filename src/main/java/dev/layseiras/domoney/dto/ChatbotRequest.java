package dev.layseiras.domoney.dto;

import java.util.List;

public record ChatbotRequest(String userInput, List<String> context) {
}
