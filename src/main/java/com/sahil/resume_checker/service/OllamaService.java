package com.sahil.resume_checker.service;


import com.sahil.resume_checker.dto.AIAnalysisResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;
import java.util.Objects;

@Service
public class OllamaService {

    private final RestClient restClient ;
    private final JsonMapper jsonMapper = JsonMapper.builder().build() ;

    public OllamaService() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:11434")
                .build();
    }


    public String generateResponse(String prompt) {

        Map<String , Object> request = Map.of(
                "model" , "gemma3:4b",
                "prompt", prompt ,
                "stream" , false
        );

        Map<?, ?> response = restClient.post()
                .uri("/api/generate")
                .body(request)
                .retrieve()
                .body(Map.class) ;

        if (response == null || response.get("response") == null){
            throw new IllegalStateException(
                    "Ollama returned an empty response."
            );
        }
        return response.get("response").toString() ;
    }

    public AIAnalysisResponse generateStructuredResponse(String prompt) {

        String jsonResponse = generateResponse(prompt);

        try {
            // Remove Markdown code fences
            jsonResponse = jsonResponse.trim();

            if (jsonResponse.startsWith("```json")) {
                jsonResponse = jsonResponse.substring(7);
            } else if (jsonResponse.startsWith("```")) {
                jsonResponse = jsonResponse.substring(3);
            }

            if (jsonResponse.endsWith("```")) {
                jsonResponse = jsonResponse.substring(
                        0, jsonResponse.length() - 3
                );
            }

            jsonResponse = jsonResponse.trim();

            // Extract JSON if the model adds text before or after it
            int start = jsonResponse.indexOf('{');
            int end = jsonResponse.lastIndexOf('}');

            if (start == -1 || end == -1 || start > end) {
                throw new IllegalStateException(
                        "Ollama did not return a valid JSON object. Response: "
                                + jsonResponse
                );
            }

            jsonResponse = jsonResponse.substring(start, end + 1);

            return jsonMapper.readValue(
                    jsonResponse,
                    AIAnalysisResponse.class
            );

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to convert Ollama response into Java object. Raw response: "
                            + jsonResponse,
                    e
            );
        }
    }

}
