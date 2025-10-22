package org.example.backend.repo;

import org.example.backend.model.WorkoutDay;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkoutDayRepo extends MongoRepository<WorkoutDay, String> {
}
