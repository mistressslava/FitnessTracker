package org.example.backend.service;

import org.example.backend.dto.WorkoutDayDto;
import org.example.backend.dto.WorkoutPlanDto;
import org.example.backend.model.*;
import org.example.backend.repo.WorkoutPlanRepo;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkoutPlanService {

    private final WorkoutPlanRepo workoutPlanRepo;
    private final IdService idService;

    public WorkoutPlanService(WorkoutPlanRepo workoutPlanRepo, IdService idService) {
        this.workoutPlanRepo = workoutPlanRepo;
        this.idService = idService;
    }

    public List<WorkoutPlan> getAllWorkoutPlans() {
        return workoutPlanRepo.findAll();
    }

    public WorkoutPlan addNewWorkoutPlan(WorkoutPlanDto workoutPlanDto) {
        List<WorkoutDay> days = safe(workoutPlanDto.days()).stream()
                .map(this::mapDayForCreate)
                .toList();
        WorkoutPlan plan = new WorkoutPlan(idService.randomId(), workoutPlanDto.title(), workoutPlanDto.description(), days);
        return workoutPlanRepo.save(plan);
    }

    public WorkoutPlan getWorkoutPlanById(String id) {
        return workoutPlanRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("WorkoutPlan with id " + id + " not found"));
    }

    public WorkoutPlan updateWorkoutPlanById(String id, WorkoutPlanDto workoutPlanDto) {
        WorkoutPlan existing = workoutPlanRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("WorkoutPlan with id " + id + " not found"));

        Map<DayOfWeek, WorkoutDay> byDayOfWeek = existing.days().stream()
                .collect(Collectors.toMap(WorkoutDay::day, d -> d));

        List<WorkoutDay> days = safe(workoutPlanDto.days()).stream()
                .map(d -> mapDayForUpdate(d, byDayOfWeek.get(d.day())))
                .toList();

        WorkoutPlan updated = new WorkoutPlan(
                existing.id(),
                workoutPlanDto.title(),
                workoutPlanDto.description(),
                days);

        return workoutPlanRepo.save(updated);
    }

    public void deleteWorkoutPlanById(String id) {
        workoutPlanRepo.deleteById(id);
    }

    private WorkoutDay mapDayForCreate(WorkoutDayDto dto) {
        if (dto.type() == WorkoutDayType.REST) {
            return new WorkoutDay(idService.randomId(), dto.day(), dto.type(), Set.of(), List.of());
        }

        List<Exercise> ex = safe(dto.exercises()).stream()
                .map(e -> new Exercise(idService.randomId(), e.name(), e.sets(), e.reps(), e.muscleGroup()))
                .toList();

        Set<MuscleGroup> targets = mergeTargets(dto.targetMuscles(), ex);
        return new WorkoutDay(idService.randomId(), dto.day(), dto.type(), targets, ex);
    }

    private WorkoutDay mapDayForUpdate(WorkoutDayDto dto, WorkoutDay existingByDow) {
        String dayId = existingByDow != null ? existingByDow.id() : idService.randomId();

        if (dto.type() == WorkoutDayType.REST) {
            return new WorkoutDay(dayId, dto.day(), dto.type(), Set.of(), List.of());
        }

        List<Exercise> ex = safe(dto.exercises()).stream()
                .map(e -> new Exercise(idService.randomId(), e.name(), e.sets(), e.reps(), e.muscleGroup()))
                .toList();

        Set<MuscleGroup> targets = mergeTargets(dto.targetMuscles(), ex);
        return new WorkoutDay(dayId, dto.day(), dto.type(), targets, ex);
    }

    private static Set<MuscleGroup> mergeTargets(Set<MuscleGroup> requested, List<Exercise> ex) {
        Set<MuscleGroup> out = new HashSet<>(requested == null ? Set.of() : requested);
        for (Exercise e : ex) if (e.muscleGroup() != null) out.add(e.muscleGroup());
        return out;
    }

    private static <T> List<T> safe(List<T> list) { return list == null ? List.of() : list; }

}
