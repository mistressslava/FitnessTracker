package org.example.backend.controller;

import org.example.backend.repo.WorkoutDayRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WorkoutDayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkoutDayRepo workoutDayRepo;

    @BeforeEach
    void setup() {
        workoutDayRepo.deleteAll();
    }

    @Test
    @DirtiesContext
    void getAllWorkoutDays_shouldReturnList() throws Exception {
        mockMvc.perform(get("/api/workout-days"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DirtiesContext
    void getAllWorkoutDays_shouldReturnEmpty_whenNoDaysExist() throws Exception {
        mockMvc.perform(get("/api/workout-days"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DirtiesContext
    void addWorkoutDay_shouldCreateWorkoutDay_whenCalled() throws Exception {

        mockMvc.perform(post("/api/workout-days")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                          {
                            "day": "MONDAY",
                            "type": "UPPER_BODY",
                            "targetMuscles": ["CHEST", "ARMS"],
                            "exercises": [
                              {
                                "name": "Bench Press",
                                "sets": 3,
                                "reps": 10,
                                "muscleGroup": "CHEST"
                              }
                            ]
                          }
                        """
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                {
                                  "day": "MONDAY",
                                  "type": "UPPER_BODY",
                                  "targetMuscles": ["CHEST", "ARMS"],
                                  "exercises": [
                                    {
                                      "name": "Bench Press",
                                      "sets": 3,
                                      "reps": 10,
                                      "muscleGroup": "CHEST"
                                    }
                                  ]
                                }
                                """
                ));
    }
}