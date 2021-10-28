package com.avidbikers.services;

import com.avidbikers.data.dto.BuyerDto;
import com.avidbikers.data.dto.SellerDto;
import com.avidbikers.data.model.Token;
import com.avidbikers.data.model.User;
import com.avidbikers.web.exceptions.AuthException;
import com.avidbikers.web.exceptions.TokenException;
import com.avidbikers.web.payload.*;


public interface AuthService {
    User registerBuyer(BuyerDto userDto) throws AuthException;
    User registerSeller(SellerDto userDto) throws AuthException;
    AuthenticationDetails login(LoginDto loginDto) throws AuthException;
    void updatePassword(PasswordRequest passwordRequest) throws AuthException;
    void resetUserPassword(PasswordResetRequest request, String passwordResetToken) throws AuthException, TokenException;
    Token generatePasswordResetToken(String username) throws AuthException;
}
