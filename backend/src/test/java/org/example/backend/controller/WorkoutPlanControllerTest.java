package org.example.backend.controller;

import org.example.backend.repo.WorkoutPlanRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class WorkoutPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkoutPlanRepo workoutPlanRepo;

    @BeforeEach
    void setup() {
        workoutPlanRepo.deleteAll();
    }

    @Test
    @DirtiesContext
    void getAllWorkoutPlans_shouldReturnEmptyList() throws Exception {
        //GIVEN

        //WHEN
        mockMvc.perform(get("/api/workout-plans"))
                //THEN
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [

                                ]
                                """
                ));
    }


    @Test
    void addNewWorkoutPLan_shouldCreateWorkoutPlan_whenCalled() throws Exception {

        mockMvc.perform(post("/api/workout-plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                        {
                            "title": "Full Week Strength Plan",
                            "description": "Balanced weekly plan focusing on both upper and lower body strength with core stability.",
                            "days": [
                              {
                                "id": "day-1",
                                "day": "MONDAY",
                                "type": "UPPER_BODY",
                                "targetMuscles": ["CHEST", "BACK", "SHOULDERS", "ARMS"],
                                "exercises": [
                                  {
                                    "id": "ex-1",
                                    "name": "Bench Press",
                                    "sets": 4,
                                    "reps": 8,
                                    "muscleGroup": "CHEST"
                                  },
                                  {
                                    "id": "ex-2",
                                    "name": "Pull-Ups",
                                    "sets": 3,
                                    "reps": 10,
                                    "muscleGroup": "BACK"
                                  }
                                ]
                              },
                              {
                                "id": "day-2",
                                "day": "TUESDAY",
                                "type": "LOWER_BODY",
                                "targetMuscles": ["LEGS", "GLUTES", "CALVES"],
                                "exercises": [
                                  {
                                    "id": "ex-3",
                                    "name": "Squats",
                                    "sets": 4,
                                    "reps": 10,
                                    "muscleGroup": "LEGS"
                                  },
                                  {
                                    "id": "ex-4",
                                    "name": "Lunges",
                                    "sets": 3,
                                    "reps": 12,
                                    "muscleGroup": "GLUTES"
                                  }
                                ]
                              },
                              {
                                "id": "day-3",
                                "day": "WEDNESDAY",
                                "type": "FULL_BODY",
                                "targetMuscles": ["CHEST", "BACK", "CORE", "CARDIO"],
                                "exercises": [
                                  {
                                    "id": "ex-5",
                                    "name": "Push-Ups",
                                    "sets": 3,
                                    "reps": 15,
                                    "muscleGroup": "CHEST"
                                  },
                                  {
                                    "id": "ex-6",
                                    "name": "Plank",
                                    "sets": 3,
                                    "reps": 60,
                                    "muscleGroup": "CORE"
                                  },
                                  {
                                    "id": "ex-7",
                                    "name": "Jump Rope",
                                    "sets": 5,
                                    "reps": 100,
                                    "muscleGroup": "CARDIO"
                                  }
                                ]
                              },
                              {
                                "id": "day-4",
                                "day": "THURSDAY",
                                "type": "REST",
                                "targetMuscles": [],
                                "exercises": []
                              },
                              {
                                "id": "day-5",
                                "day": "FRIDAY",
                                "type": "REST",
                                "targetMuscles": [],
                                "exercises": []
                              },
                              {
                                "id": "day-6",
                                "day": "SATURDAY",
                                "type": "REST",
                                "targetMuscles": [],
                                "exercises": []
                              },
                              {
                                "id": "day-7",
                                "day": "SUNDAY",
                                "type": "REST",
                                "targetMuscles": [],
                                "exercises": []
                              }
                            ]
                        }
                        """
                ))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                        {
                            "title": "Full Week Strength Plan",
                            "description": "Balanced weekly plan focusing on both upper and lower body strength with core stability.",
                            "days": [
                              {
                                "id": "day-1",
                                "day": "MONDAY",
                                "type": "UPPER_BODY",
                                "targetMuscles": ["CHEST", "BACK", "SHOULDERS", "ARMS"],
                                "exercises": [
                                  {
                                    "id": "ex-1",
                                    "name": "Bench Press",
                                    "sets": 4,
                                    "reps": 8,
                                    "muscleGroup": "CHEST"
                                  },
                                  {
                                    "id": "ex-2",
                                    "name": "Pull-Ups",
                                    "sets": 3,
                                    "reps": 10,
                                    "muscleGroup": "BACK"
                                  }
                                ]
                              },
                              {
                                "id": "day-2",
                                "day": "TUESDAY",
                                "type": "LOWER_BODY",
                                "targetMuscles": ["LEGS", "GLUTES", "CALVES"],
                                "exercises": [
                                  {
                                    "id": "ex-3",
                                    "name": "Squats",
                                    "sets": 4,
                                    "reps": 10,
                                    "muscleGroup": "LEGS"
                                  },
                                  {
                                    "id": "ex-4",
                                    "name": "Lunges",
                                    "sets": 3,
                                    "reps": 12,
                                    "muscleGroup": "GLUTES"
                                  }
                                ]
                              },
                              {
                                "id": "day-3",
                                "day": "WEDNESDAY",
                                "type": "FULL_BODY",
                                "targetMuscles": ["CHEST", "BACK", "CORE", "CARDIO"],
                                "exercises": [
                                  {
                                    "id": "ex-5",
                                    "name": "Push-Ups",
                                    "sets": 3,
                                    "reps": 15,
                                    "muscleGroup": "CHEST"
                                  },
                                  {
                                    "id": "ex-6",
                                    "name": "Plank",
                                    "sets": 3,
                                    "reps": 60,
                                    "muscleGroup": "CORE"
                                  },
                                  {
                                    "id": "ex-7",
                                    "name": "Jump Rope",
                                    "sets": 5,
                                    "reps": 100,
                                    "muscleGroup": "CARDIO"
                                  }
                                ]
                              },
                              {
                                "id": "day-4",
                                "day": "THURSDAY",
                                "type": "REST",
                                "targetMuscles": [],
                                "exercises": []
                              },
                              {
                                "id": "day-5",
                                "day": "FRIDAY",
                                "type": "REST",
                                "targetMuscles": [],
                                "exercises": []
                              },
                              {
                                "id": "day-6",
                                "day": "SATURDAY",
                                "type": "REST",
                                "targetMuscles": [],
                                "exercises": []
                              },
                              {
                                "id": "day-7",
                                "day": "SUNDAY",
                                "type": "REST",
                                "targetMuscles": [],
                                "exercises": []
                              }
                            ]
                        }
                        """
                ));
    }
}