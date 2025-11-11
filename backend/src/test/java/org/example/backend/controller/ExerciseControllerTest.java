package org.example.backend.controller;

import org.example.backend.model.Exercise;
import org.example.backend.model.MuscleGroup;
import org.example.backend.repo.ExerciseRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
    @WithMockUser
    @DirtiesContext
    void getAllExercises_shouldReturnListOfExercises() throws Exception {
        //GIVEN
        Exercise exercise1 = new Exercise("1", "Exercise1", 3, 12, MuscleGroup.ARMS);
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
    @WithMockUser
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
    @WithMockUser
    @DirtiesContext
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
                .andExpect(status().isCreated())
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
    @WithMockUser
    @DirtiesContext
    void addNewExercise_shouldReturn400_whenEmptyExerciseField() throws Exception {
        //WHEN
        mockMvc.perform(post("/api/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                            {
                                              "name": "",
                                              "sets": 3,
                                              "reps": 12
                                            }
                                        """
                        ))
                //THEN
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Exercise name is required! Please enter a name.")
                );
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void addNewExercise_shouldReturn400_whenIllegalArgument() throws Exception {
        //WHEN
        mockMvc.perform(post("/api/exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                            {
                                              "name": "Exercise1",
                                              "sets": -1,
                                              "reps": 12
                                            }
                                        """
                        ))
                //THEN
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Sets must be > 0")
                );
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void updateExerciseById_shouldUpdateExercise() throws Exception {
        //GIVEN
        Exercise exercise = new Exercise("1", "Exercise 1", 3, 10, MuscleGroup.ARMS);
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
    @WithMockUser
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
    @WithMockUser
    @DirtiesContext
    void deleteExercise_shouldDeleteExerciseById_status204() throws Exception {
        //GIVEN
        Exercise exercise = new Exercise("1", "Exercise 1", 3, 10, MuscleGroup.ARMS);
        exerciseRepo.save(exercise);

        //WHEN
        mockMvc.perform(delete("/api/exercises/1"))
                //THEN
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void deleteExercise_whenIdNotFound_shouldThrowException() throws Exception {
        //WHEN
        mockMvc.perform(delete("/api/exercises/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Exercise with id 1 not found"));
    }

    @Test
    @WithMockUser
    @DirtiesContext
    void getExerciseById_shouldReturnExercise_whenIdExist() throws Exception {
        //GIVEN
        Exercise exercise = new Exercise("1", "Exercise 1", 3, 10, MuscleGroup.ARMS);
        exerciseRepo.save(exercise);
        //WHEN
        mockMvc.perform(get("/api/exercises/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                    "name": "Exercise 1",
                                     "sets": 3,
                                     "reps": 10
                                 }
                                 """))
                //THEN
                .andExpect(content().json("""
                                 {
                                    "id": "1",
                                    "name": "Exercise 1",
                                    "sets": 3,
                                    "reps": 10
                                 }
                                 """))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    @DirtiesContext
    void getExerciseById_shouldThrowException_whenIdNotExist() throws Exception {
        //WHEN
        mockMvc.perform(get("/api/exercises/1"))
                //THEN
                .andExpect(content().string("Exercise with id 1 not found"))
                .andExpect(status().isNotFound());
    }
}