package org.example.backend.controller;

import org.example.backend.model.Exercise;
import org.example.backend.model.MuscleGroup;
import org.example.backend.model.WorkoutDay;
import org.example.backend.model.WorkoutDayType;
import org.example.backend.repo.WorkoutDayRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

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
                .andExpect(status().isCreated())
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

    @Test
    @DirtiesContext
    void deleteWorkout_shouldDeleteExerciseById_status204() throws Exception {
        //GIVEN
        Exercise exercise = new Exercise("1", "exercise1", 4, 12, MuscleGroup.ARMS);
        WorkoutDay expected = new WorkoutDay(
                "1",
                DayOfWeek.FRIDAY,
                WorkoutDayType.FULL_BODY,
                Set.of(MuscleGroup.BACK),
                List.of(exercise));
        workoutDayRepo.save(expected);

        //WHEN
        mockMvc.perform(delete("/api/workout-days/1"))
                //THEN
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void deleteWorkout_whenIdNotFound_shouldThrowException() throws Exception {
        //WHEN
        mockMvc.perform(delete("/api/exercises/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Exercise with id 1 not found"));
    }

    @Test
    @DirtiesContext
    void updateWorkoutDayById_shouldUpdateWorkoutDay() throws Exception {
        //GIVEN
        Exercise exercise = new Exercise("1", "Bench Press", 3, 10, MuscleGroup.CHEST);
        WorkoutDay expected = new WorkoutDay(
                "1",
                DayOfWeek.MONDAY,
                WorkoutDayType.UPPER_BODY,
                Set.of(MuscleGroup.CHEST),
                List.of(exercise));
        workoutDayRepo.save(expected);

        //WHEN
        mockMvc.perform(put("/api/workout-days/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         {
                            "day": "MONDAY",
                            "type": "UPPER_BODY",
                            "targetMuscles": ["CHEST"],
                            "exercises": [
                              {
                                "name": "Bench Press",
                                "sets": 4,
                                "reps": 12,
                                "muscleGroup": "CHEST"
                              }
                            ]
                          }
                         """))
                //THEN
                .andExpect(content().json("""
                        {
                            "day": "MONDAY",
                            "type": "UPPER_BODY",
                            "targetMuscles": ["CHEST"],
                            "exercises": [
                              {
                                "name": "Bench Press",
                                "sets": 4,
                                "reps": 12,
                                "muscleGroup": "CHEST"
                              }
                            ]
                          }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void updateWorkoutDayById_shouldThrowException_whenIdNotFound() throws Exception {
        //WHEN
        mockMvc.perform(put("/api/workout-days/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "day": "MONDAY",
                            "type": "UPPER_BODY",
                            "targetMuscles": ["CHEST"],
                            "exercises": [
                              {
                                "name": "Bench Press",
                                "sets": 4,
                                "reps": 12,
                                "muscleGroup": "CHEST"
                              }
                            ]
                          }
                        """))
                //THEN
                .andExpect(status().isNotFound())
                .andExpect(content().string("Workout day with id 1 not found"));
    }
}