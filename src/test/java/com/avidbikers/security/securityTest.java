package com.avidbikers.security;

import com.avidbikers.data.model.Role;
import com.avidbikers.data.model.User;
import com.avidbikers.data.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class securityTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
    private User mockedUser;

    @BeforeEach
    void setUp() {
        mockedUser = new User();
        mockedUser.setFirstName("Ismail");
        mockedUser.setLastName("Abdullah");
        mockedUser.setEmail("ohida2001@gmail.com");
        mockedUser.setPassword("pass1234");
        Role role = new Role();
        role.setName("BUYER");
        mockedUser.getRoles().add(role);
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        userRepository = null;
        customUserDetailsService = null;
    }

    /**
     * User details service test
     */

    @Test
    @DisplayName("User details can be fetch from database by email with role User")
    void user_canFetchDataFromDbByEmail() {
        when(userRepository.findByEmail("ohida2001@gmail.com"))
                .thenReturn(Optional.of(mockedUser));

        UserPrincipal fetchedUser = (UserPrincipal) customUserDetailsService.loadUserByUsername("ohida2001@gmail.com");

        verify(userRepository, times(1)).findByEmail("ohida2001@gmail.com");

        assertNotNull(fetchedUser);
        assertAll(
                () -> assertEquals(fetchedUser.getFirstName(), mockedUser.getFirstName()),
                () -> assertEquals(fetchedUser.getLastName(), mockedUser.getLastName()),
                () -> assertEquals(fetchedUser.getEmail(), mockedUser.getEmail()),
                () -> assertEquals(fetchedUser.getUsername(), mockedUser.getFirstName() + " " + mockedUser.getLastName()),
                () -> assertEquals(fetchedUser.getPassword(), mockedUser.getPassword()),
                () -> assertEquals(fetchedUser.getAuthorities().size(), 1)
        );
    }

    /**
     * Jwt Token Test
     */

    @Test
    @DisplayName("Jwt token can be generated")
    void jwt_tokenCanBeGenerated() {
        //Given
        when(userRepository.findByEmail("ohida2001@gmail.com"))
                .thenReturn(Optional.of(mockedUser));

        //When
        UserPrincipal fetchedUser = (UserPrincipal) customUserDetailsService.loadUserByUsername("ohida2001@gmail.com");
        String actualToken = jwtTokenProvider.generateToken(fetchedUser);

        //Assert
        assertNotNull(actualToken);
        assertEquals(actualToken.getClass(), String.class);
    }

    @Test
    @DisplayName("Username can be extracted from jwt token")
    void can_extractUsernameFromJwtToken() {
        String expected = mockedUser.getFirstName() + " " + mockedUser.getLastName();
        //Given
        when(userRepository.findByEmail("ohida2001@gmail.com"))
                .thenReturn(Optional.of(mockedUser));

        //When
        UserPrincipal fetchedUser = (UserPrincipal) customUserDetailsService.loadUserByUsername("ohida2001@gmail.com");
        String jwtToken = jwtTokenProvider.generateToken(fetchedUser);
        String actual = jwtTokenProvider.extractEmail(jwtToken);

        //Assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Token can be validated by checking expiration date")
    void test_thatTokenHasNotExpire() {
        //Given
        when(userRepository.findByEmail("ohida2001@gmail.com"))
                .thenReturn(Optional.of(mockedUser));

        //When
        UserPrincipal fetchedUser = (UserPrincipal) customUserDetailsService.loadUserByUsername("ohida2001@gmail.com");
        String jwtToken = jwtTokenProvider.generateToken(fetchedUser);
        boolean hasExpire = jwtTokenProvider.isTokenExpired(jwtToken);

        //Assert
        assertFalse(hasExpire);
    }

    @Test
    @DisplayName("Jwt token can be validated by username and expiration date")
    void test_jwtTokenCanBeValidated() {
        //Given
        when(userRepository.findByEmail("ohida2001@gmail.com"))
                .thenReturn(Optional.of(mockedUser));

        //When
        UserPrincipal fetchedUser = (UserPrincipal) customUserDetailsService.loadUserByUsername("ohida2001@gmail.com");
        String jwtToken = jwtTokenProvider.generateToken(fetchedUser);
        boolean isValid = jwtTokenProvider.validateToken(jwtToken, fetchedUser);

        //Assert
        assertFalse(isValid);
    }
}