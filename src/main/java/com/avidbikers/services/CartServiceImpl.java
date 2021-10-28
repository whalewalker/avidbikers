package com.avidbikers.services;

import com.avidbikers.data.dto.CartDto;
import com.avidbikers.data.model.Cart;
import com.avidbikers.data.model.Item;
import com.avidbikers.data.model.Product;
import com.avidbikers.data.repository.CartRepository;
import com.avidbikers.web.exceptions.CartException;
import com.avidbikers.web.exceptions.ProductException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.math.BigDecimal;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductService productService;

    @Override
    public Cart createCart() {
        return createNewCart();
    }

    private Cart createNewCart() {
        return cartRepository.save(new Cart());
    }

    @Override
    public BigDecimal calculateCartTotal(String cartId) throws CartException {
        Cart cart = findById(cartId);
        BigDecimal total = BigDecimal.ZERO;
        for (Item cartItem: cart.getItems().values()) {
            total = total.add(cartItem.getItemTotal());
        }
        return total;
    }

    @Override
    public void addItemToCart(String productId, int quantity, String cartId) throws ProductException, CartException {
        Product product = productService.findProduct(productId);
        Cart cart = findById(cartId);
        cart.addItem(product, quantity);
        saveCart(cart);
    }

    private void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(String cartId, String productId) throws CartException {
        Cart cart = findById(cartId);
        cart.removeItem(productId);
        saveCart(cart);
    }

    @Override
    public CartDto findCartById(String cartId) throws CartException {
        Cart cart = findById(cartId);
        return modelMapper.map(cart, CartDto.class);
    }

    private Cart findById(String cartId) throws CartException {
        return cartRepository.findById(cartId).orElseThrow(
                () -> new CartException("No cart found with id" + cartId));
    }

    @Override
    public void reduceCartItemQuantity(String cartId, String productId, int quantity) throws CartException {
        Cart cart = findById(cartId);
        cart.removeItem(productId, quantity);
        saveCart(cart);
    }
}
