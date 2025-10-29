package org.example.backend.planGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.exception.ChatGPTRequestException;
import org.example.backend.model.WorkoutPlan;
import org.example.backend.repo.WorkoutPlanRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class WorkoutPlanGenerationService {

    private final RestClient restClient; //ChatClient
    private final WorkoutPlanRepo repo;
    private final ObjectMapper mapper;

    public WorkoutPlanGenerationService(
            RestClient.Builder builder,
            @Value("${app.openai-api-key}") String openaiApiKey,
            @Value("${app.openai-base-url:https://api.openai.com/v1}") String baseUrl,
            WorkoutPlanRepo repo,
            ObjectMapper mapper) {
        this.restClient = builder
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + openaiApiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.repo = repo;
        this.mapper = mapper;
    }

    public WorkoutPlan generatePlanAndSave(String prompt) {

        var messages = List.of(
                new ChatGPTRequestMessage("system",
                        """
                                Respond ONLY with valid JSON. Use EXACTLY these 7 days in this order.
                                 Populate fields but NEVER change the day values.
                                
                                 {
                                   "title": "...",
                                   "description": "...",
                                   "days": [
                                     { "day": "MONDAY",    "type": "...", "targetMuscles": [...], "exercises": [...] },
                                     { "day": "TUESDAY",   "type": "...", "targetMuscles": [...], "exercises": [...] },
                                     { "day": "WEDNESDAY", "type": "...", "targetMuscles": [...], "exercises": [...] },
                                     { "day": "THURSDAY",  "type": "...", "targetMuscles": [...], "exercises": [...] },
                                     { "day": "FRIDAY",    "type": "...", "targetMuscles": [...], "exercises": [...] },
                                     { "day": "SATURDAY",  "type": "...", "targetMuscles": [...], "exercises": [...] },
                                     { "day": "SUNDAY",    "type": "...", "targetMuscles": [...], "exercises": [...] }
                                   ]
                                 }
                                
                                 Hard rules:
                                 - Keep all 7 entries and keep day EXACTLY as listed (no duplicates).
                                 - If type = REST => exercises = [] and targetMuscles = [].
                                 - Use UPPERCASE enums exactly as specified.
                                 - type ∈ { UPPER_BODY, LOWER_BODY, FULL_BODY, REST }.
                                 - CORE is allowed on UPPER_BODY and LOWER_BODY days (as accessory work).
                                 - CARDIO and MOBILITY are only allowed on FULL_BODY days (never on UPPER/LOWER/REST).
                                
                                """),
                new ChatGPTRequestMessage("user", prompt)
        );

        var schema = Map.of(
                "name", "WorkoutPlan",
                "strict", true,
                "schema", Map.of(
                        "type", "object",
                        "additionalProperties", false,
                        "required", List.of("title", "description", "days"),
                        "properties", Map.of(
                                "title", Map.of("type", "string"),
                                "description", Map.of("type", "string"),
                                "days", Map.of(
                                        "type", "array",
                                        "minItems", 7,
                                        "maxItems", 7,
                                        "items", Map.of(
                                                "type", "object",
                                                "additionalProperties", false,
                                                "required", List.of("day", "type", "targetMuscles", "exercises"),
                                                "properties", Map.of(
                                                        "day", Map.of(
                                                                "type", "string",
                                                                "enum", List.of(
                                                                        "MONDAY", "TUESDAY", "WEDNESDAY",
                                                                        "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"
                                                                )
                                                        ),
                                                        "type", Map.of(
                                                                "type", "string",
                                                                "enum", List.of("UPPER_BODY", "LOWER_BODY", "FULL_BODY", "REST")
                                                        ),
                                                        "targetMuscles", Map.of(
                                                                "type", "array",
                                                                "items", Map.of(
                                                                        "type", "string",
                                                                        "enum", List.of(
                                                                                "CHEST", "BACK", "SHOULDERS", "ARMS",
                                                                                "LEGS", "GLUTES", "CALVES", "CORE",
                                                                                "CARDIO", "MOBILITY"
                                                                        )
                                                                )
                                                        ),
                                                        "exercises", Map.of(
                                                                "type", "array",
                                                                "items", Map.of(
                                                                        "type", "object",
                                                                        "additionalProperties", false,
                                                                        "required", List.of("name", "sets", "reps", "muscleGroup"),
                                                                        "properties", Map.of(
                                                                                "name", Map.of("type", "string"),
                                                                                "sets", Map.of("type", "integer", "minimum", 1),
                                                                                "reps", Map.of("type", "integer", "minimum", 1),
                                                                                "muscleGroup", Map.of(
                                                                                        "type", "string",
                                                                                        "enum", List.of(
                                                                                                "CHEST", "BACK", "SHOULDERS", "ARMS",
                                                                                                "LEGS", "GLUTES", "CALVES", "CORE",
                                                                                                "CARDIO", "MOBILITY"
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        var req = Map.of(
                "model", "gpt-5",
                "messages", messages,
                "response_format", Map.of(
                        "type", "json_schema",
                        "json_schema", schema
                )
        );



        ChatGPTResponse resp;
        try {
            resp = restClient.post()
                    .uri("/chat/completions")
                    .body(req)
                    .retrieve()
                    .body(ChatGPTResponse.class);
        } catch (RestClientResponseException e) {
            throw new ChatGPTRequestException("OpenAI error: " + e.getResponseBodyAsString());
        }

        if (resp == null || resp.choices() == null || resp.choices().isEmpty()) {
            throw new ChatGPTRequestException("Empty OpenAI response");
        }

        String json = resp.text().trim();

        try {
            WorkoutPlan plan = mapper.readValue(json, WorkoutPlan.class);
            return repo.save(plan);

        } catch (Exception e) {
            throw new ChatGPTRequestException("Invalid JSON from OpenAI: " + e.getMessage());
        }
    }
}






