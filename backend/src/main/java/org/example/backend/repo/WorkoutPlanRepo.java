package org.example.backend.repo;

import org.example.backend.model.WorkoutPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutPlanRepo extends MongoRepository<WorkoutPlan, String> {
}
