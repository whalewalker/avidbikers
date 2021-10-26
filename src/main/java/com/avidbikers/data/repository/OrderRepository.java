package com.avidbikers.data.repository;


import com.avidbikers.data.model.Address;
import com.avidbikers.data.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {
    void findByOrderId(String userId);
    Optional<Order> findByDeliveryAddress(Address address);
}
