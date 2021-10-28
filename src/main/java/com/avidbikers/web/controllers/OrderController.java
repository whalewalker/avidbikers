package com.avidbikers.web.controllers;

import com.avidbikers.data.dto.OrderDto;
import com.avidbikers.data.model.Address;
import com.avidbikers.services.OrderService;
import com.avidbikers.web.exceptions.CartException;
import com.avidbikers.web.exceptions.OrderException;
import com.avidbikers.web.payload.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/avidbikers/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @GetMapping("/{orderId}")
    public ResponseEntity<?> findOrderById(@PathVariable String orderId){
        try{
            OrderDto orderDto = orderService.findOrderById(orderId);
            return new ResponseEntity<>(orderDto, HttpStatus.OK);
        } catch (CartException | OrderException orderException) {
            return new ResponseEntity<>(new ApiResponse(false, "No order found by that orderID"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{deliveryAddress}")
    public ResponseEntity<?> findOrderByDeliveryAddress(@PathVariable Address deliveryAddress){
        try{
            OrderDto orderDto = orderService.findOrderByDeliveryAddress(deliveryAddress);
            return new ResponseEntity<>(orderDto, HttpStatus.OK);
        } catch (OrderException orderException) {
            return new ResponseEntity<>(new ApiResponse(false, "No order found by that deliveryAddress"), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<?> updateOrderDetails(@PathVariable String orderId, @RequestBody OrderDto updatedInformation){
        try{
            OrderDto updatedOrder =orderService.updateOrderDetails(orderId, updatedInformation);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);

        } catch (OrderException orderException){
            return new ResponseEntity<>(new ApiResponse(false, "No order found with that ID"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderId){

        try{
            orderService.cancelOrderByOrderId(orderId);
            return new ResponseEntity<>(new ApiResponse(true, "Deleted Successfully"), HttpStatus.NO_CONTENT);
        }catch (OrderException orderException){
            return new ResponseEntity<>(new ApiResponse(false, orderException.getMessage()),HttpStatus.BAD_REQUEST);
        }
    }

}