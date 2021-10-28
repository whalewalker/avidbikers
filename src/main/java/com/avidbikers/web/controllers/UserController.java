package com.avidbikers.web.controllers;

import com.avidbikers.data.dto.BuyerDto;
import com.avidbikers.data.dto.CartDto;
import com.avidbikers.data.dto.ProductDto;
import com.avidbikers.data.model.Address;
import com.avidbikers.data.model.User;
import com.avidbikers.services.ProductService;
import com.avidbikers.services.UserService;
import com.avidbikers.web.exceptions.CartException;
import com.avidbikers.web.exceptions.ProductException;
import com.avidbikers.web.exceptions.UserException;
import com.avidbikers.web.payload.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/avidbikers/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId){
        try {
            BuyerDto buyerDto = userService.findUserById(userId);
            return new ResponseEntity<>(buyerDto, HttpStatus.OK);
        } catch (UserException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("{userId}/cart")
    public ResponseEntity<?> getUserCart(@PathVariable String userId) {
        try {
            CartDto cartDTO = userService.getUserCart(userId);
            return new ResponseEntity<>(cartDTO, HttpStatus.OK);
        } catch (UserException | CartException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage())
                    , HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{userId}/cart/add/{productId}/{quantity}")
    public ResponseEntity<?> addProductToCart(@PathVariable String userId, @PathVariable String productId, @PathVariable String quantity) {
        try {
            int quantityValue = Integer.parseInt(quantity);
            userService.addProductToCart(productId, userId, quantityValue);
            return new ResponseEntity<>(new ApiResponse(true, "Product Added Successfully"), HttpStatus.OK);
        } catch (ProductException | UserException | CartException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{userId}/cart/remove/{productId}/{quantity}")
    public ResponseEntity<?> removeProductFromCart(@PathVariable String userId, @PathVariable String productId, @PathVariable String quantity) {
        try {
            int quantityValue = Integer.parseInt(quantity);
            userService.removeProductFromCart(productId, userId, quantityValue);
            return new ResponseEntity<>(new ApiResponse(true, "Product Removed Successfully"), HttpStatus.OK);
        } catch (UserException | CartException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("{userId}/addresses/add")
    public ResponseEntity<?> addAddress(@RequestBody Address address, @PathVariable String userId) {
        try {
            userService.addAddress(userId, address);
            return new ResponseEntity<>(new ApiResponse(true, "Address added successfully"),HttpStatus.OK );
        } catch (UserException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("{userId}/addresses")
    public ResponseEntity<?> getAddresses(@PathVariable String userId) {
        try {
            List<Address> addresses = userService.getUserAddresses(userId);
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }



}
