package com.avidbikers.data.repository;

import com.avidbikers.data.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
