package com.avidbikers.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private String orderId;
    @DBRef
    private Address deliveryAddress;
    private BigDecimal orderTotal;
    private String userId;
}
