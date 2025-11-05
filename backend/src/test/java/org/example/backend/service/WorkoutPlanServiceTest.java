package org.example.backend.service;

import org.example.backend.dto.ExerciseDto;
import org.example.backend.dto.WorkoutDayDto;
import org.example.backend.dto.WorkoutPlanDto;
import org.example.backend.model.*;
import org.example.backend.repo.WorkoutPlanRepo;
import org.example.backend.util.TestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WorkoutPlanServiceTest {

    private WorkoutPlanRepo workoutPlanRepo;
    private WorkoutPlanService workoutPlanService;
    private IdService idService;

    @BeforeEach
    void setup() {
        workoutPlanRepo = mock(WorkoutPlanRepo.class);
        idService = mock(IdService.class);
        workoutPlanService = new WorkoutPlanService(workoutPlanRepo, idService);
    }

    @Test
    void getAllWorkoutPlans_shouldReturnEmptyList() {
        //GIVEN
        when(workoutPlanRepo.findAll()).thenReturn(Collections.emptyList());

        //WHEN
        List<WorkoutPlan> actualList = workoutPlanService.getAllWorkoutPlans();

        //THEN
        assertTrue(actualList.isEmpty());
        verify(workoutPlanRepo).findAll();
    }

    @Test
    void getAllWorkoutPlans_shouldReturnList() {
        //GIVEN

        when(workoutPlanRepo.findAll()).thenReturn(List.of(
                TestFixtures.weekPlan("1"),
                TestFixtures.weekPlan("2")
        ));
        //WHEN
        List<WorkoutPlan> actualList = workoutPlanService.getAllWorkoutPlans();
        //THEN
        assertEquals(2, actualList.size());
        verify(workoutPlanRepo).findAll();
    }

    @Test
    void addNewWorkoutPlan_mapsDto_andGeneratesIds() {
        WorkoutPlanDto dto = new WorkoutPlanDto(
                "Test description",
                "Weekly Split",
                List.of(
                        new WorkoutDayDto(DayOfWeek.MONDAY,    WorkoutDayType.REST,       Set.of(), List.of()),
                        new WorkoutDayDto(DayOfWeek.TUESDAY,   WorkoutDayType.UPPER_BODY, Set.of(MuscleGroup.BACK),
                                List.of(new ExerciseDto("Row", 4, 10, MuscleGroup.BACK))),
                        new WorkoutDayDto(DayOfWeek.WEDNESDAY, WorkoutDayType.REST,       Set.of(), List.of()),
                        new WorkoutDayDto(DayOfWeek.THURSDAY,  WorkoutDayType.LOWER_BODY, Set.of(MuscleGroup.LEGS),
                                List.of(new ExerciseDto("Squat", 5, 5, MuscleGroup.LEGS))),
                        new WorkoutDayDto(DayOfWeek.FRIDAY,    WorkoutDayType.REST,       Set.of(), List.of()),
                        new WorkoutDayDto(DayOfWeek.SATURDAY,  WorkoutDayType.REST,       Set.of(), List.of()),
                        new WorkoutDayDto(DayOfWeek.SUNDAY,    WorkoutDayType.REST,       Set.of(), List.of())
                )
        );

        // IMPORTANT: stub id generator so ids aren't null
        when(idService.randomId()).thenAnswer(inv -> java.util.UUID.randomUUID().toString());

        when(workoutPlanRepo.save(any(WorkoutPlan.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        WorkoutPlan actual = workoutPlanService.addNewWorkoutPlan(dto);

        assertNotNull(actual.id());
        assertEquals("Test description", actual.title());
        assertEquals("Weekly Split", actual.description());
        assertEquals(7, actual.days().size());

        WorkoutDay mon = actual.days().get(0);
        assertEquals(DayOfWeek.MONDAY, mon.day());
        assertEquals(WorkoutDayType.REST, mon.type());
        assertNotNull(mon.id());
        assertTrue(mon.exercises().isEmpty());
        assertTrue(mon.targetMuscles().isEmpty());

        WorkoutDay tue = actual.days().get(1);
        assertEquals(DayOfWeek.TUESDAY, tue.day());
        assertEquals(WorkoutDayType.UPPER_BODY, tue.type());
        assertNotNull(tue.id());
        assertEquals(1, tue.exercises().size());
        assertNotNull(tue.exercises().get(0).id());

        WorkoutDay thu = actual.days().get(3);
        assertEquals(DayOfWeek.THURSDAY, thu.day());
        assertEquals(WorkoutDayType.LOWER_BODY, thu.type());
        assertNotNull(thu.id());
        assertEquals(1, thu.exercises().size());
        assertNotNull(thu.exercises().get(0).id());

        verify(workoutPlanRepo).save(any(WorkoutPlan.class));
        verifyNoMoreInteractions(workoutPlanRepo);
    }


    @Test
    void updateWorkoutPlanById_shouldThrowException_whenMissing() {
        WorkoutPlanDto dto = new WorkoutPlanDto(
                "Test description",
                "Weekly Split",
                List.of(
                        new WorkoutDayDto(DayOfWeek.MONDAY, WorkoutDayType.REST, Set.of(), List.of())
                )
        );

        when(workoutPlanRepo.findById("1")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> workoutPlanService.updateWorkoutPlanById("1", dto));

        verify(workoutPlanRepo).findById("1");
        verifyNoMoreInteractions(workoutPlanRepo);
    }

    @Test
    void updateWorkoutPlanById_shouldUpdate_andKeepDayIdsByDayOfWeek() {
        WorkoutPlan existing = new WorkoutPlan(
                "plan-1",
                "Old title",
                "Old descr",
                List.of(
                        new WorkoutDay("d-mon", DayOfWeek.MONDAY, WorkoutDayType.REST, Set.of(), List.of()),
                        new WorkoutDay("d-tue", DayOfWeek.TUESDAY, WorkoutDayType.UPPER_BODY,
                                Set.of(MuscleGroup.BACK),
                                List.of(new Exercise("e-row", "Row", 4, 10, MuscleGroup.BACK))
                        ),
                        new WorkoutDay("d-wen", DayOfWeek.WEDNESDAY, WorkoutDayType.REST, Set.of(), List.of()),
                        new WorkoutDay("d-thur", DayOfWeek.THURSDAY, WorkoutDayType.LOWER_BODY,
                                Set.of(MuscleGroup.LEGS),
                                List.of(new Exercise("e-squat", "Squat", 5, 5, MuscleGroup.LEGS))
                        ),
                        new WorkoutDay("d-thur", DayOfWeek.FRIDAY, WorkoutDayType.REST, Set.of(), List.of()),
                        new WorkoutDay("d-thur", DayOfWeek.SATURDAY, WorkoutDayType.REST, Set.of(), List.of()),
                        new WorkoutDay("d-thur", DayOfWeek.SUNDAY, WorkoutDayType.REST, Set.of(), List.of())

                )
        );

        when(workoutPlanRepo.findById("plan-1")).thenReturn(Optional.of(existing));
        when(workoutPlanRepo.save(any(WorkoutPlan.class))).thenAnswer(inv -> inv.getArgument(0));

        WorkoutPlanDto dto = new WorkoutPlanDto(
                "Updated title",
                "Weekly Split",
                List.of(
                        new WorkoutDayDto(
                                DayOfWeek.MONDAY,
                                WorkoutDayType.REST,
                                Set.of(),
                                List.of()
                        ),
                        new WorkoutDayDto(
                                DayOfWeek.TUESDAY,
                                WorkoutDayType.UPPER_BODY,
                                Set.of(MuscleGroup.BACK),
                                List.of(new ExerciseDto("Row", 4, 10, MuscleGroup.BACK))
                        ),
                        new WorkoutDayDto(
                                DayOfWeek.WEDNESDAY,
                                WorkoutDayType.REST,
                                Set.of(),
                                List.of()
                        ),
                        new WorkoutDayDto(
                                DayOfWeek.THURSDAY,
                                WorkoutDayType.LOWER_BODY,
                                Set.of(MuscleGroup.LEGS),
                                List.of(new ExerciseDto("Squat", 5, 5, MuscleGroup.LEGS))
                        ),
                        new WorkoutDayDto(
                                DayOfWeek.FRIDAY,
                                WorkoutDayType.REST,
                                Set.of(),
                                List.of()
                        ),
                        new WorkoutDayDto(
                                DayOfWeek.SATURDAY,
                                WorkoutDayType.REST,
                                Set.of(),
                                List.of()
                        ),
                        new WorkoutDayDto(
                                DayOfWeek.SUNDAY,
                                WorkoutDayType.REST,
                                Set.of(),
                                List.of()
                        )
                )
        );

        WorkoutPlan actual = workoutPlanService.updateWorkoutPlanById("plan-1", dto);

        assertEquals("plan-1", actual.id());
        assertEquals("Updated title", actual.title());
        assertEquals(7, actual.days().size());

        WorkoutDay mon = actual.days().stream().filter(d -> d.day() == DayOfWeek.MONDAY).findFirst().orElseThrow();
        WorkoutDay tue = actual.days().stream().filter(d -> d.day() == DayOfWeek.TUESDAY).findFirst().orElseThrow();

        assertEquals("d-mon", mon.id());
        assertEquals("d-tue", tue.id());

        assertEquals(WorkoutDayType.REST, mon.type());
        assertTrue(mon.exercises().isEmpty());
        assertTrue(mon.targetMuscles().isEmpty());

        assertEquals(WorkoutDayType.UPPER_BODY, tue.type());
        assertEquals(1, tue.exercises().size());
        assertEquals("Row", tue.exercises().getFirst().name());
        assertTrue(tue.targetMuscles().contains(MuscleGroup.BACK));

        verify(workoutPlanRepo).findById("plan-1");
        verify(workoutPlanRepo).save(any(WorkoutPlan.class));
        verifyNoMoreInteractions(workoutPlanRepo);
    }

}