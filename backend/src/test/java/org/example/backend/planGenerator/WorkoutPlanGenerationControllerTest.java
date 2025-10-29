package org.example.backend.planGenerator;

import org.example.backend.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class WorkoutPlanGenerationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkoutPlanGenerationService service;

    @Test
    void generatePlan_shouldBeSuccessful() throws Exception {
        when(service.generatePlanAndSave(any()))
                .thenReturn(new WorkoutPlan("id-1", "Full Week", "desc",
                        List.of(
                                new WorkoutDay("1", DayOfWeek.MONDAY, WorkoutDayType.REST, Set.of(), List.of()),
                                new WorkoutDay("2", DayOfWeek.TUESDAY, WorkoutDayType.UPPER_BODY, Set.of(MuscleGroup.BACK),
                                        List.of(new Exercise("ex-1", "Row", 4, 10, MuscleGroup.BACK))),
                                new WorkoutDay("3", DayOfWeek.WEDNESDAY, WorkoutDayType.REST, Set.of(), List.of()),
                                new WorkoutDay("4", DayOfWeek.THURSDAY, WorkoutDayType.LOWER_BODY, Set.of(MuscleGroup.LEGS),
                                        List.of(new Exercise("ex-2", "Squat", 5, 5, MuscleGroup.LEGS))),
                                new WorkoutDay("5", DayOfWeek.FRIDAY, WorkoutDayType.REST, Set.of(), List.of()),
                                new WorkoutDay("6", DayOfWeek.SATURDAY, WorkoutDayType.FULL_BODY, Set.of(MuscleGroup.CORE),
                                        List.of(new Exercise("ex-3", "Plank", 3, 60, MuscleGroup.CORE))),
                                new WorkoutDay("7", DayOfWeek.SUNDAY, WorkoutDayType.REST, Set.of(), List.of())
                        )));

        mockMvc.perform(post("/api/generate-plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"prompt": "for beginner, 7 days"}
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void generatePlan_shouldThrowException() throws Exception {

        mockMvc.perform(post("/api/generate-plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"prompt": null}
                                """))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @Test
    void generatePlan_shouldThrowException_returns400() throws Exception {
        when(service.generatePlanAndSave(any()))
                .thenThrow(new IllegalArgumentException("Prompt is missing or invalid JSON"));

        mockMvc.perform(post("/api/generate-plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"prompt": null}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Prompt is missing or invalid JSON"));
        verifyNoInteractions(service);
    }
}