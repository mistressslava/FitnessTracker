package org.example.backend.controller;

import org.example.backend.repo.WorkoutPlanRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
class WorkoutPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkoutPlanRepo workoutPlanRepo;

    @Test
    @DirtiesContext
    void getAllWorkoutPlans_shouldReturnEmptyList() throws Exception {
        //GIVEN

        //WHEN
        mockMvc.perform(get("/api/workout-plans"))
                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                [

                                ]
                                """
                ));
    }
}