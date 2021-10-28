package com.avidbikers.services;

import com.avidbikers.data.dto.CartDto;
import com.avidbikers.data.dto.BuyerDto;
import com.avidbikers.data.model.Address;
import com.avidbikers.data.model.User;
import com.avidbikers.data.repository.UserRepository;
import com.avidbikers.web.exceptions.CartException;
import com.avidbikers.web.exceptions.ProductException;
import com.avidbikers.web.exceptions.UserException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartService cartService;


    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CartDto getUserCart(String userId) throws UserException, CartException {
        String cartId = findAUserById(userId).getCartId();
        return cartService.findCartById(cartId);
    }

    private User findAUserById(String userId) throws UserException {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserException("No User Found with that Id"));
    }

    @Override
    public BuyerDto findUserById(String userId) throws UserException {
        User user = findAUserById(userId);
        return modelMapper.map(user, BuyerDto.class);
    }

    @Override
    public void addAddress(String userId, Address address) throws UserException {
        User user = findAUserById(userId);
        user.getAddresses().add(address);
        saveUser(user);
    }

    private void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void addProductToCart(String productId, String userId, int quantity) throws UserException, ProductException, CartException {
        User user = findAUserById(userId);
        String cartId = user.getCartId();
        cartService.addItemToCart(productId,quantity, cartId);
    }

    @Override
    public void removeProductFromCart(String productId, String userId, int quantity) throws UserException, CartException {
        User user = findAUserById(userId);
        String cartId = user.getCartId();
        cartService.removeItemFromCart(cartId, productId);
    }

    @Override
    public List<Address> getUserAddresses(String userId) throws UserException {
        User user = findAUserById(userId);
        return user.getAddresses();
    }
}
