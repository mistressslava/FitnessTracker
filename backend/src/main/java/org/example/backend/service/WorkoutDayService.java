package org.example.backend.service;

import org.example.backend.dto.WorkoutDayDto;
import org.example.backend.model.WorkoutDay;
import org.example.backend.repo.WorkoutDayRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class WorkoutDayService {

    private final WorkoutDayRepo workoutDayRepo;
    private final IdService idService;

    public WorkoutDayService(WorkoutDayRepo workoutDayRepo, IdService idService) {
        this.workoutDayRepo = workoutDayRepo;
        this.idService = idService;
    }

    public List<WorkoutDay> getAllWorkoutDays() {
        return workoutDayRepo.findAll();
    }

    public WorkoutDay addWorkoutDay(WorkoutDayDto workoutDayDto) {
        WorkoutDay workoutDay = new WorkoutDay(
                idService.randomId(),
                workoutDayDto.day(),
                workoutDayDto.type(),
                workoutDayDto.targetMuscles(),
                workoutDayDto.exercises());
        return workoutDayRepo.save(workoutDay);
    }

    public WorkoutDay updateWorkoutDayById(String id, WorkoutDayDto workoutDayDto) {
        WorkoutDay existing = workoutDayRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Workout day with id " + id + " not found"));

        WorkoutDay updated = new WorkoutDay(
                existing.id(),
                workoutDayDto.day(),
                workoutDayDto.type(),
                workoutDayDto.targetMuscles(),
                workoutDayDto.exercises());

        return workoutDayRepo.save(updated);
    }

    public void deleteWorkoutDay(String id) {
        WorkoutDay existing = workoutDayRepo.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Exercise with id " + id + " not found"));
        workoutDayRepo.deleteById(existing.id());
    }
}
