package org.example.backend.controller;

import org.example.backend.dto.WorkoutDayDto;
import org.example.backend.model.WorkoutDay;
import org.example.backend.service.WorkoutDayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<WorkoutDay> addWorkoutDay(@RequestBody WorkoutDayDto workoutDayDto) {
        WorkoutDay newDay = workoutDayService.addWorkoutDay(workoutDayDto);
        return ResponseEntity.created(URI.create("/api/workout-days/" + newDay.id()))
                .body(newDay);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutDay> updateWorkoutDayById(@PathVariable String id, @RequestBody WorkoutDayDto workoutDayDto) {
        WorkoutDay updatedWorkoutDay = workoutDayService.updateWorkoutDayById(id, workoutDayDto);
        return ResponseEntity.ok(updatedWorkoutDay);
    }

    @DeleteMapping("/{id}")
    public void deleteWorkoutDay(@PathVariable String id) {
        workoutDayService.deleteWorkoutDay(id);
    }

}
