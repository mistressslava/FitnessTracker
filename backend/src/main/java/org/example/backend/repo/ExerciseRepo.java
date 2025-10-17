package org.example.backend.repo;

import org.example.backend.model.Exercise;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExerciseRepo extends MongoRepository<Exercise, String> {
}
