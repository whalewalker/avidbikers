package com.avidbikers.data.repository;

import com.avidbikers.data.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, String> {
}
