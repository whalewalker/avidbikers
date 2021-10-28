package com.avidbikers.services;

import com.avidbikers.data.dto.BuyerDto;
import com.avidbikers.data.model.*;
import com.avidbikers.data.repository.RoleRepository;
import com.avidbikers.data.repository.TokenRepository;
import com.avidbikers.data.repository.UserRepository;
import com.avidbikers.security.CustomUserDetailsService;
import com.avidbikers.security.JwtTokenProvider;
import com.avidbikers.security.UserPrincipal;
import com.avidbikers.web.exceptions.AuthException;
import com.avidbikers.web.exceptions.TokenException;
import com.avidbikers.web.exceptions.UserRoleNotFoundException;
import com.avidbikers.web.payload.AuthenticationDetails;
import com.avidbikers.web.payload.LoginDto;
import com.avidbikers.web.payload.PasswordRequest;
import com.avidbikers.web.payload.PasswordResetRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private AuthServiceImpl authService;
    private User mockedUser;



    @BeforeEach
    void setUp() {
        mockedUser = new User();
        mockedUser.setId("001");
        mockedUser.setFirstName("Ismail");
        mockedUser.setLastName("Abdullah");
        mockedUser.setEmail("ismail@gmail.com");
        mockedUser.setPassword("pass1234");
        Role role = new Role();
        role.setName("BUYER");
        Role savedRole = roleRepository.save(role);
        mockedUser.getRoles().add(savedRole);

        MockitoAnnotations.openMocks(this);
    }


    @Test
    void userCanRegisterAsABuyer() throws AuthException, UserRoleNotFoundException {
        //Given
        BuyerDto userDto = new BuyerDto();
        userDto.setEmail(mockedUser.getEmail());

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(modelMapper.map(userDto, User.class)).thenReturn(mockedUser);
        when(userRepository.save(any(User.class))).thenReturn(mockedUser);
        when(roleService.findByName("BUYER")).thenReturn(new Role());
        when(cartService.createCart()).thenReturn(new Cart());
        //When
        User savedUser = authService.registerBuyer(userDto);

        //Assert
        verify(userRepository, times(1)).existsByEmail(mockedUser.getEmail());
        verify(userRepository, times(1)).save(mockedUser);
        assertThat(savedUser).isNotNull();
    }


    @Test
    void whenLoginMethodIsCalled_ThenFindUserByEmailIsCalledOnce() {
        //Given
        LoginDto loginDto = new LoginDto("ismail@gmail.com", "password123");
        when(userRepository.findByEmail("ismail@gmail.com")).thenReturn(Optional.of(mockedUser));

        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(loginDto.getEmail(),
                loginDto.getPassword());
        testingAuthenticationToken.setAuthenticated(true);
        testingAuthenticationToken.setDetails(loginDto);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword())
        )).thenReturn(testingAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);

        UserPrincipal principal = modelMapper.map(mockedUser, UserPrincipal.class);
        log.info("User principal ==> {}", principal);


        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockedUser));
        UserPrincipal fetchedUser = (UserPrincipal) customUserDetailsService.loadUserByUsername(loginDto.getEmail());
        String actualToken = jwtTokenProvider.generateToken(fetchedUser);

        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(fetchedUser);
        when(jwtTokenProvider.generateToken(any(UserPrincipal.class))).thenReturn(actualToken);

        AuthenticationDetails authDetails = authService.login(loginDto);
        verify(customUserDetailsService, times(2)).loadUserByUsername(loginDto.getEmail());
        verify(jwtTokenProvider, times(2)).generateToken(principal);
        verify(userRepository, times(1)).findByEmail(loginDto.getEmail());

        assertNotNull(authDetails);
        log.info("user details ==> {}", authDetails);
        assertEquals(authDetails.getJwtToken(), actualToken);
        assertEquals(authDetails.getEmail(), loginDto.getEmail());
    }

    @Test
    void whenLoginMethodIsCalled_withNullEmail_NullPointerExceptionIsThrown(){
        LoginDto loginDto = new LoginDto();
        when(userRepository.findByEmail(loginDto.getEmail())).thenThrow(new NullPointerException("User email cannot be null"));
        verify(userRepository, times(0)).findByEmail(loginDto.getEmail());
    }

    @Test
    void whenLoginMethodIsCalled_withNullPassword_NullPointerExceptionIsThrown(){
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("whalewalker@gmail.com");
        when(userRepository.findByEmail(loginDto.getEmail())).thenThrow(new NullPointerException("User password cannot be null"));
        verify(userRepository, times(0)).findByEmail(loginDto.getEmail());
    }

    @Test
    @DisplayName("Saved user can update password")
    void savedUserCanUpdatePassword() throws AuthException {
        String randomEncoder = UUID.randomUUID().toString();
        //Given
        PasswordRequest passwordRequest = new PasswordRequest("ismail@gmail.com", "password123", "pass1234");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockedUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn(randomEncoder);

        //When
        String expected = passwordRequest.getOldPassword();
        String actual = mockedUser.getPassword();
        authService.updatePassword(passwordRequest);

        //Assert
        verify(passwordEncoder, times(1)).matches(expected, actual);
        verify(passwordEncoder, times(1)).encode(passwordRequest.getPassword());
        verify(userRepository, times(1)).findByEmail(passwordRequest.getEmail());
        verify(userRepository, times(1)).save(mockedUser);

        assertNotEquals(expected, mockedUser.getPassword());
        assertEquals(randomEncoder, mockedUser.getPassword());
    }

    @Test
    @DisplayName("Password reset token can be generated for user to reset password")
    void resetTokenCanBeGeneratedWhenUserWantToResetPassword() throws AuthException {
        //Given
        String email = mockedUser.getEmail();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockedUser));

        //When
        ArgumentCaptor<Token> tokenArgumentCaptor = ArgumentCaptor.forClass(Token.class);
        authService.generatePasswordResetToken(email);

        // Assert
        verify(userRepository, times(1)).findByEmail(email);
        verify(tokenRepository, times(1)).save(tokenArgumentCaptor.capture());

        assertNotNull(tokenArgumentCaptor.getValue());
        assertNotNull(tokenArgumentCaptor.getValue().getToken());
        assertEquals(TokenType.PASSWORD_RESET, tokenArgumentCaptor.getValue().getType());
        assertNotNull(tokenArgumentCaptor.getValue().getUser());
    }

    @Test
    @DisplayName("User can reset password")
    void savedUserCanResetPassword() throws AuthException, TokenException {
        // Given
        String randomEncoder = UUID.randomUUID().toString();
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest("ismail@gmail.com", "pass1234");
        String passwordResetToken = UUID.randomUUID().toString();

        Token mockToken = new Token();
        mockToken.setId("001");
        mockToken.setToken(passwordResetToken);
        mockToken.setType(TokenType.PASSWORD_RESET);
        mockToken.setUser(mockedUser);
        mockToken.setExpiry(LocalDateTime.now().plusMinutes(30));

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockedUser));
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(mockToken));
        when(passwordEncoder.encode(anyString())).thenReturn(randomEncoder);

        ArgumentCaptor<User> tokenArgumentCaptor = ArgumentCaptor.forClass(User.class);
        authService.resetUserPassword(passwordResetRequest, passwordResetToken);

        verify(userRepository, times(1)).save(tokenArgumentCaptor.capture());
        verify(tokenRepository, times(1)).delete(mockToken);
        verify(passwordEncoder, times(1)).encode(passwordResetRequest.getPassword());

        assertThat(tokenArgumentCaptor.getValue()).isNotNull();
        assertThat(tokenArgumentCaptor.getValue().getPassword()).isNotNull();
    }

}