package com.avidbikers.web.controllers;

import com.avidbikers.data.dto.BuyerDto;
import com.avidbikers.data.model.Token;
import com.avidbikers.data.repository.UserRepository;
import com.avidbikers.services.AuthService;
import com.avidbikers.web.payload.LoginDto;
import com.avidbikers.web.payload.PasswordRequest;
import com.avidbikers.web.payload.PasswordResetRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private String registerJsonObject;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthService authService;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        BuyerDto userDto = new BuyerDto();
        userDto.setFirstName("Ismail");
        userDto.setLastName("Abdul");
        userDto.setEmail("ismail1@gmail.com");
        userDto.setPassword("password123");
        userDto.setPhoneNumber("09075617573");

        registerJsonObject = objectMapper.writeValueAsString(userDto);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void whenUserRegisterWithValidInput_thenReturns201() throws Exception {
        mockMvc.perform(post("/api/v1/avidbikers/auth/register")
                        .contentType("application/json")
                        .content(registerJsonObject)).andDo(print())
                .andExpect(status().isCreated());
    }


    @Test
    void whenUserLoginWithValidInput_thenReturns200() throws Exception {
        //Given
        LoginDto loginDto = new LoginDto("test@gmail.com", "test123");

        //When
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/avidbikers/auth/login")
                        .contentType("application/json").content(objectMapper.writeValueAsString(loginDto)))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        //Then
        int expectedStatus = 200;
        int actualStatus = mvcResult.getResponse().getStatus();
        assertThat(expectedStatus).isEqualTo(actualStatus);
    }



    @Test
    void whenUserUpdatePasswordWithValidInput_thenReturns200() throws Exception {
        PasswordRequest passwordRequest = new PasswordRequest("user@gmail.com", "pass123", "password123");
        mockMvc.perform(post("/api/v1/avidbikers/auth/password/update")
                        .contentType("application/json").content(objectMapper.writeValueAsString(passwordRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenUserForgetPassword_thenReturn201() throws Exception{
        when(authService.generatePasswordResetToken(anyString())).thenReturn(new Token());
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/avidbikers/auth/password/reset/whale")
                .contentType("application.json"))
                .andDo(print())
                .andExpect(status().isCreated()).andReturn();

        //Then
        int expectedStatus = 201;
        int actualStatus = mvcResult.getResponse().getStatus();
        assertThat(expectedStatus).isEqualTo(actualStatus);
    }

    @Test
    void whenUserResetPassword_theReturn200() throws Exception{
        //Given
        PasswordResetRequest passwordReset = new PasswordResetRequest("test@gmail.com", "test123");

        //When
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/avidbikers/auth/password/reset/93j34fh8wnj43n8a")
                        .contentType("application/json").content(objectMapper.writeValueAsString(passwordReset)))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        //Then
        int expectedStatus = 200;
        int actualStatus = mvcResult.getResponse().getStatus();
        assertThat(expectedStatus).isEqualTo(actualStatus);
    }


}