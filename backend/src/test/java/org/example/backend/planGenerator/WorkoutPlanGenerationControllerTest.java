package org.example.backend.planGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
class WorkoutPlanGenerationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    @WithMockUser
    @DirtiesContext
    void generatePlan_shouldBeSuccessful() throws Exception {
        //GIVEN
        String planJson = """
        {
          "title": "Full Week Strength Plan",
          "description": "Balanced weekly plan focusing on both upper and lower body strength with core stability.",
          "days": [
            { "id":"day-1","day":"MONDAY","type":"UPPER_BODY","targetMuscles":["CHEST","BACK","SHOULDERS","ARMS"],"exercises":[] },
            { "id":"day-2","day":"TUESDAY","type":"LOWER_BODY","targetMuscles":["LEGS","GLUTES","CALVES"],"exercises":[] },
            { "id":"day-3","day":"WEDNESDAY","type":"FULL_BODY","targetMuscles":["CHEST","BACK","CORE","CARDIO"],"exercises":[] },
            { "id":"day-4","day":"THURSDAY","type":"REST","targetMuscles":[],"exercises":[] },
            { "id":"day-5","day":"FRIDAY","type":"REST","targetMuscles":[],"exercises":[] },
            { "id":"day-6","day":"SATURDAY","type":"REST","targetMuscles":[],"exercises":[] },
            { "id":"day-7","day":"SUNDAY","type":"REST","targetMuscles":[],"exercises":[] }
          ]
        }
        """;

        String contentString = new ObjectMapper().writeValueAsString(planJson);

        String openAiLike = """
               {
                "choices": [
                  { "message": { "content": %s } }
                ]
               }
               """.formatted(contentString);

        mockServer.expect(requestTo("https://api.openai.com/v1/chat/completions"))
                .andExpect(header("Authorization", "Bearer test-key"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(openAiLike, MediaType.APPLICATION_JSON));

        //WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/generate-plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                  {"prompt": "for beginner, 7 days"}
                                """))

                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Full Week Strength Plan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.days[0].type").value("UPPER_BODY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.days.length()").value(7))

                .andExpect(status().isOk());
        mockServer.verify();
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void generatePlan_shouldThrowException() throws Exception {
        //GIVEN

        // WHEN
        mockMvc.perform(MockMvcRequestBuilders.post("/api/generate-plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                  {"prompt": null}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Prompt is missing or invalid JSON"));

        mockServer.verify();
    }
}