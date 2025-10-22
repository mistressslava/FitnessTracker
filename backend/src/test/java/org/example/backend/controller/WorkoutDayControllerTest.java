package org.example.backend.controller;

import org.example.backend.repo.WorkoutDayRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

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
    void getAllWorkoutDays() {

    }

    @Test
    @DirtiesContext
    void addWorkoutDay() {
    }
}