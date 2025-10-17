package org.example.backend.controller;

import org.example.backend.model.Exercise;
import org.example.backend.repo.ExerciseRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExerciseRepo exerciseRepo;

    @BeforeEach
    void setup() {
        exerciseRepo.deleteAll();
    }

    @Test
    @DirtiesContext
    void getAllExercises_shouldReturnListOfExercises() throws Exception {
        //GIVEN
        Exercise exercise1 = new Exercise("1", "Exercise1", 3, 12);
        exerciseRepo.save(exercise1);
        //WHEN
        mockMvc.perform(get("/api/exercises"))
                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                          {
                            "id": "1",
                            "name": "Exercise1",
                            "sets": 3,
                            "reps": 12
                          }
                        ]
                        """));
    }

    @Test
    @DirtiesContext
    void getAllExercises_shouldReturnEmptyList_whenNoExercisesAdded() throws Exception {
        //GIVEN
        //WHEN
        mockMvc.perform(get("/api/exercises"))
                //THEN
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                        ]
                        """));
    }
}