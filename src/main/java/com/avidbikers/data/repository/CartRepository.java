package com.avidbikers.data.repository;

import com.avidbikers.data.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {

}
