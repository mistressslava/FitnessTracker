package org.example.backend.controller;

import org.example.backend.dto.WorkoutDayDto;
import org.example.backend.model.WorkoutDay;
import org.example.backend.service.WorkoutDayService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout-days")
public class WorkoutDayController {

    private final WorkoutDayService workoutDayService;


    public WorkoutDayController(WorkoutDayService workoutDayService) {
        this.workoutDayService = workoutDayService;
    }

    @GetMapping
    public List<WorkoutDay> getAllWorkoutDays() {
        return workoutDayService.getAllWorkoutDays();
    }

    @PostMapping
    public WorkoutDay addWorkoutDay(@RequestBody WorkoutDayDto workoutDayDto) {
        return workoutDayService.addWorkoutDay(workoutDayDto);
    }
}
