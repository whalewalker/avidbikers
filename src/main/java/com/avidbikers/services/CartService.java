package com.avidbikers.services;

import com.avidbikers.data.dto.CartDto;
import com.avidbikers.data.model.Cart;
import com.avidbikers.web.exceptions.CartException;
import com.avidbikers.web.exceptions.ProductException;

import java.math.BigDecimal;

public interface CartService {
    Cart createCart();
    BigDecimal calculateCartTotal(String cartId) throws CartException;
    void addItemToCart(String productId, int quantity, String cartId) throws ProductException, CartException;
    void removeItemFromCart(String cartId, String productId) throws CartException;
    CartDto findCartById(String cartId) throws CartException;
    void reduceCartItemQuantity(String cartId, String productId, int quantity) throws CartException;
}
