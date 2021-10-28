package com.avidbikers.services;

import com.avidbikers.data.dto.OrderDto;
import com.avidbikers.data.model.Address;
import com.avidbikers.data.model.Order;
import com.avidbikers.data.repository.OrderRepository;
import com.avidbikers.web.exceptions.CartException;
import com.avidbikers.web.exceptions.OrderException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OrderDto findOrderById(String orderId) throws OrderException {
        Order order = findById(orderId);
        return modelMapper.map(order, OrderDto.class);
    }

    private Order findById(String orderId) throws OrderException {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new OrderException("No order found with that Id" + orderId));
    }

    @Override
    public OrderDto findOrderByDeliveryAddress(Address delivery) throws OrderException {
        Order order = orderRepository.findByDeliveryAddress(delivery).orElseThrow(
                () -> new OrderException("No Order found with this address" + delivery));
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public OrderDto updateOrderDetails(String orderId, OrderDto orderDto) throws OrderException {
        Order orderToUpdate = findById(orderId);
        modelMapper.map(orderDto, orderToUpdate);
        orderRepository.save(orderToUpdate);
        return modelMapper.map(orderToUpdate, OrderDto.class);
    }

    @Override
    public void cancelOrderByOrderId(String orderId) throws OrderException {
        Order orderToCancel = findById(orderId);
        deleteOrder(orderToCancel);
    }

    private void deleteOrder(Order orderToCancel) {
        orderRepository.delete(orderToCancel);
    }
}
