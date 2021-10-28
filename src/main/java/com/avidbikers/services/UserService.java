package com.avidbikers.services;

import com.avidbikers.data.dto.CartDto;
import com.avidbikers.data.dto.BuyerDto;
import com.avidbikers.data.model.Address;
import com.avidbikers.web.exceptions.CartException;
import com.avidbikers.web.exceptions.ProductException;
import com.avidbikers.web.exceptions.UserException;

import java.util.List;

public interface UserService {


    CartDto getUserCart(String userId) throws UserException, CartException;

    BuyerDto findUserById(String userId) throws UserException;

    void addAddress(String userId, Address address) throws UserException;

    void addProductToCart(String productId, String userId, int quantity) throws UserException, ProductException, CartException;

    void removeProductFromCart(String productId, String userId, int quantity) throws UserException, CartException;

    List<Address> getUserAddresses(String userId) throws UserException;
}
