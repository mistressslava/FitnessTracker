package org.example.backend.service;

import org.example.backend.dto.WorkoutDayDto;
import org.example.backend.model.Exercise;
import org.example.backend.model.MuscleGroup;
import org.example.backend.model.WorkoutDay;
import org.example.backend.model.WorkoutDayType;
import org.example.backend.repo.WorkoutDayRepo;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

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
        return workoutDayRepo.save(mapDayForCreate(workoutDayDto));
    }

    public WorkoutDay updateWorkoutDayById(String id, WorkoutDayDto workoutDayDto) {
        WorkoutDay existing = workoutDayRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Workout day with id " + id + " not found"));

        WorkoutDay updated = mapDayForUpdate(workoutDayDto, existing);
        updated = new WorkoutDay(
                existing.id(),
                updated.day(),
                updated.type(),
                updated.targetMuscles(),
                updated.exercises());

        return workoutDayRepo.save(updated);
    }

    public void deleteWorkoutDay(String id) {
        WorkoutDay existing = workoutDayRepo.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Exercise with id " + id + " not found"));
        workoutDayRepo.deleteById(existing.id());
    }

    private WorkoutDay mapDayForCreate(WorkoutDayDto dto) {
        if (dto.type() == WorkoutDayType.REST) {
            return new WorkoutDay(idService.randomId(), dto.day(), dto.type(), Set.of(), List.of());
        }
        List<Exercise> ex = dto.exercises().stream()
                .map(e -> new Exercise(
                        idService.randomId(),
                        e.name(), e.sets(),
                        e.reps(),
                        e.muscleGroup()))
                .toList();
        var targets = mergeTargets(dto.targetMuscles(), ex);
        return new WorkoutDay(idService.randomId(), dto.day(), dto.type(), targets, ex);
    }

    private WorkoutDay mapDayForUpdate(WorkoutDayDto dto, WorkoutDay existing) {
        String id = existing != null ? existing.id() : idService.randomId();

        if (dto.type() == WorkoutDayType.REST) {
            return new WorkoutDay(id, dto.day(), dto.type(), Set.of(), List.of());
        }
        List<Exercise> ex = dto.exercises().stream()
                .map(e -> new Exercise(
                        idService.randomId(),
                        e.name(), e.sets(),
                        e.reps(),
                        e.muscleGroup()))
                .toList();
        Set<MuscleGroup> targets = mergeTargets(dto.targetMuscles(), ex);
        return new WorkoutDay(id, dto.day(), dto.type(), targets, ex);
    }

    private Set<MuscleGroup> mergeTargets(Set<MuscleGroup> requested, List<Exercise> ex) {
        Set<MuscleGroup> out = new HashSet<>(requested == null ? Set.of() : requested);
        for (Exercise item : ex) if (item.muscleGroup() != null) out.add(item.muscleGroup());
        return out;
    }
}
