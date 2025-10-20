package org.example.backend.controller;

import org.example.backend.model.Exercise;
import org.example.backend.repo.ExerciseRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .andExpect(status().isOk())
                .andExpect(content().json("""
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
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void addNewExercise_shouldReturnAddedExercise() throws Exception {
        //WHEN
        mockMvc.perform(post("/api/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                            {
                                              "name": "Exercise1",
                                              "sets": 3,
                                              "reps": 12
                                            }
                                        """
                        ))
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                    {
                                      "name": "Exercise1",
                                      "sets": 3,
                                      "reps": 12
                                    }
                                
                                """
                ));
    }

    @Test
    @DirtiesContext
    void updateExerciseById_shouldUpdateExercise() throws Exception {
        //GIVEN
        Exercise exercise = new Exercise("1", "Exercise 1", 3, 10);
        exerciseRepo.save(exercise);

        //WHEN
        mockMvc.perform(put("/api/exercises/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                           {
                            "name": "Updated Exercise 1",
                            "sets": 5,
                            "reps": 10
                           }
                        """)
        )
                //THEN
                .andExpect(content().json("""
                           {
                              "id": "1",
                              "name": "Updated Exercise 1",
                              "sets": 5,
                              "reps": 10
                           }
                          """))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void updateExerciseById_shouldThrowException_whenIdNotFound() throws Exception {
        //WHEN
        mockMvc.perform(put("/api/exercises/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         {
                            "name": "Updated Exercise 1",
                            "sets": 5,
                            "reps": 10
                         }
                         """))
                //THEN
                .andExpect(status().isNotFound())
                .andExpect(content().string("Exercise with id 1 not found"));
    }

    @Test
    @DirtiesContext
    void deleteExercise_shouldDeleteExerciseById_status204() throws Exception {
        //GIVEN
        Exercise exercise = new Exercise("1", "Exercise 1", 3, 10);
        exerciseRepo.save(exercise);

        //WHEN
        mockMvc.perform(delete("/api/exercises/1"))
                //THEN
                .andExpect(status().isNoContent());
    }

    @Test
    @DirtiesContext
    void deleteExercise_whenIdNotFound_shouldThrowException() throws Exception {
        //WHEN
        mockMvc.perform(delete("/api/exercises/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Exercise with id 1 not found"));
    }
}