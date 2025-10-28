package org.example.backend.planGenerator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.model.WorkoutPlan;
import org.example.backend.repo.WorkoutPlanRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class WorkoutPlanGenerationServiceTest {

    private RestClient.Builder builder;
    private MockRestServiceServer server;

    private ObjectMapper mapper;
    private WorkoutPlanRepo repo;
    private WorkoutPlanGenerationService service;

    @BeforeEach
    void setup() {
        builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        repo = mock(WorkoutPlanRepo.class);
        mapper = new ObjectMapper();
        service = new WorkoutPlanGenerationService(builder, "test-key", "http://mock", repo, mapper);
    }

    @Test
    void generatePlanAndSaveShouldBeSuccessful() throws JsonProcessingException {

        Map<String, Object> plan = new HashMap<>();
        plan.put("title", "Full Week");
        plan.put("description", "D");
        plan.put("days", List.of(
                Map.of("day","MONDAY",    "type","REST",       "targetMuscles", List.of(), "exercises", List.of()),
                Map.of("day","TUESDAY",   "type","UPPER_BODY", "targetMuscles", List.of("BACK"), "exercises", List.of()),
                Map.of("day","WEDNESDAY", "type","REST",       "targetMuscles", List.of(), "exercises", List.of()),
                Map.of("day","THURSDAY",  "type","LOWER_BODY", "targetMuscles", List.of("LEGS"), "exercises", List.of()),
                Map.of("day","FRIDAY",    "type","REST",       "targetMuscles", List.of(), "exercises", List.of()),
                Map.of("day","SATURDAY",  "type","FULL_BODY",  "targetMuscles", List.of("CORE"), "exercises", List.of()),
                Map.of("day","SUNDAY",    "type","REST",       "targetMuscles", List.of(), "exercises", List.of())
        ));

        String contentString = mapper.writeValueAsString(plan);

        Map<String, Object> outer = Map.of(
                "choices", List.of(
                        Map.of("message", Map.of("content", contentString))
                )
        );
        String openAiLike = mapper.writeValueAsString(outer);

        System.out.println(openAiLike);


        server.expect(requestTo("http://mock/chat/completions"))
                .andExpect(header("Authorization", "Bearer test-key"))
                .andRespond(withSuccess(openAiLike, MediaType.APPLICATION_JSON));

        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));


        var saved = service.generatePlanAndSave("for beginner, 7 days");

        assertEquals("Full Week", saved.title());
        verify(repo).save(any());
        server.verify();
    }
}