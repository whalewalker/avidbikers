package com.avidbikers.services;

import com.avidbikers.data.dto.UserDto;
import com.avidbikers.data.model.Token;
import com.avidbikers.data.model.TokenType;
import com.avidbikers.data.model.User;
import com.avidbikers.data.repository.TokenRepository;
import com.avidbikers.data.repository.UserRepository;
import com.avidbikers.security.CustomUserDetailsService;
import com.avidbikers.security.JwtTokenProvider;
import com.avidbikers.security.UserPrincipal;
import com.avidbikers.web.exceptions.AuthException;
import com.avidbikers.web.exceptions.TokenException;
import com.avidbikers.web.payload.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.avidbikers.data.model.Role.USER;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public User register(UserDto userDto) throws AuthException {
        if (existByEmail(userDto.getEmail())) {
            throw new AuthException("Email is already in use");
        }
        User newUser = modelMapper.map(userDto, User.class);
        newUser.getRoles().add(USER);
        return userRepository.save(newUser);
    }

    public boolean existByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    @Override
    public AuthenticationDetails login(LoginDto loginDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserPrincipal userDetails = (UserPrincipal) customUserDetailsService.loadUserByUsername(loginDto.getEmail());
        final String token = jwtTokenProvider.generateToken(userDetails);
        User user = internalFindUserByEmail(loginDto.getEmail());
        return new AuthenticationDetails(token, user.getEmail());
    }

    private User internalFindUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    private User save(User user) {
        return userRepository.save(user);
    }

    private boolean validateEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void updatePassword(PasswordRequest request) throws AuthException {
        String email = request.getEmail();
        String oldPassword = request.getOldPassword();
        String newPassword = request.getPassword();
        User userToChangePassword = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("No user found with email" + email));

        boolean passwordMatch = passwordEncoder.matches(oldPassword, userToChangePassword.getPassword());
        if (!passwordMatch) {
            throw new AuthException("Passwords do not match");
        }
        userToChangePassword.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userToChangePassword);
    }

    @Override
    public void resetUserPassword(PasswordResetRequest request, String passwordResetToken) throws AuthException, TokenException {
        String email = request.getEmail();
        String newPassword = request.getPassword();
        User userToResetPassword = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("No user found with user name " + email));
        Token token = tokenRepository.findByToken(passwordResetToken)
                .orElseThrow(() -> new TokenException(String.format("No token with value %s found", passwordResetToken)));
        if (token.getExpiry().isBefore(LocalDateTime.now())) {
            throw new TokenException("This password reset token has expired ");
        }
        if (!token.getUser().getId().equals(userToResetPassword.getId())) {
            throw new TokenException("This password rest token does not belong to this user");
        }
        userToResetPassword.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userToResetPassword);
        tokenRepository.delete(token);
    }


    @Override
    public Token generatePasswordResetToken(String email) throws AuthException {
        User userToResetPassword = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("No user found with user name " + email));
        Token token = new Token();
        token.setType(TokenType.PASSWORD_RESET);
        token.setUser(userToResetPassword);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiry(LocalDateTime.now().plusMinutes(30));
        return tokenRepository.save(token);
    }

}
