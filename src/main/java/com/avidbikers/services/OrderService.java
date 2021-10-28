package com.avidbikers.services;

import com.avidbikers.data.dto.OrderDto;
import com.avidbikers.data.model.Address;
import com.avidbikers.web.exceptions.CartException;
import com.avidbikers.web.exceptions.OrderException;

public interface OrderService {
    OrderDto findOrderById(String orderId) throws CartException, OrderException;
    OrderDto findOrderByDeliveryAddress(Address delivery) throws OrderException;
    OrderDto updateOrderDetails(String orderId, OrderDto orderToUpdate) throws OrderException;
    void cancelOrderByOrderId(String orderId) throws OrderException;
}
