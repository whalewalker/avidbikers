package com.avidbikers.web.controllers;

import com.avidbikers.data.dto.UserDto;
import com.avidbikers.data.repository.UserRepository;
import com.avidbikers.services.AuthService;
import com.avidbikers.web.exceptions.ControllerExceptionHandler;
import com.avidbikers.web.payload.LoginDto;
import com.avidbikers.web.payload.PasswordRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
        UserDto userDto = new UserDto();
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
    void whenUserRegisterWithValidInput_thenReturns200() throws Exception {
        mockMvc.perform(post("/api/v1/avidbikers/auth/register")
                        .contentType("application/json")
                        .content(registerJsonObject)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenUserProvideNullValue_thenReturns400AndErrorResult() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setFirstName("Ismail");
        userDto.setLastName("Abdul");

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/avidbikers/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest()).andReturn();

        ControllerExceptionHandler.ErrorResult expectedErrorResponse =
                new ControllerExceptionHandler.ErrorResult("phoneNumber", "phone number cannot be blank");

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(expectedErrorResponse);

        assertThat(actualResponseBody).isEqualToIgnoringCase(expectedResponseBody);
    }

    @Test
    void whenValidInput_thenMapToUserDtoModel() throws Exception {
        mockMvc.perform(post("/api/v1/avidbikers/auth/register")
                        .contentType("application/json")
                        .content(registerJsonObject)).andDo(print())
                .andExpect(status().isOk());

        ArgumentCaptor<UserDto> userDtoArgumentCaptor = ArgumentCaptor.forClass(UserDto.class);
        verify(authService, times(1)).register(userDtoArgumentCaptor.capture());
        assertThat(userDtoArgumentCaptor.getValue().getFirstName()).isEqualTo("Ismail");
        assertThat(userDtoArgumentCaptor.getValue().getEmail()).isEqualTo("ismail1@gmail.com");
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
    void whenUserLoginWithPasswordCharacterLesserThanSix_thenReturns400AndErrorResult() throws Exception {
        LoginDto loginDto = new LoginDto("test@gmail.com", "12345");
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/ormCloudSync/auth/login")
                        .contentType("application/json").content(objectMapper.writeValueAsString(loginDto)))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn();

        ControllerExceptionHandler.ErrorResult expectedErrorResponse =
                new ControllerExceptionHandler.ErrorResult("password", "Invalid password");

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(expectedErrorResponse);

        assertThat(actualResponseBody).isEqualToIgnoringCase(expectedResponseBody);
    }

    @Test
    void whenUserLoginWithInValidEmail_thenReturns400AndErrorResult() throws Exception {
        LoginDto loginDto = new LoginDto("testgmail.com", "1234545");
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/ormCloudSync/auth/login")
                        .contentType("application/json").content(objectMapper.writeValueAsString(loginDto)))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn();

        ControllerExceptionHandler.ErrorResult expectedErrorResponse =
                new ControllerExceptionHandler.ErrorResult("email", "Invalid email");

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(expectedErrorResponse);

        assertThat(actualResponseBody).isEqualToIgnoringCase(expectedResponseBody);
    }

    @Test
    void whenUserUpdatePasswordWithValidInput_thenReturns200() throws Exception {
        PasswordRequest passwordRequest = new PasswordRequest("user@gmail.com", "pass123", "password123");
        mockMvc.perform(post("/api/v1/ormCloudSync/auth/password/update")
                        .contentType("application/json").content(objectMapper.writeValueAsString(passwordRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenUserUpdatePasswordWithValidInput_thenReturns400() throws Exception {
        PasswordRequest passwordRequest = new PasswordRequest("whalewalker", "pass123", "password123");

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/ormCloudSync/auth/password/update")
                        .contentType("application/json").content(objectMapper.writeValueAsString(passwordRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn();

        ControllerExceptionHandler.ErrorResult expectedErrorResponse =
                new ControllerExceptionHandler.ErrorResult("email", "Email must be valid");

        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        String expectedResponseBody = objectMapper.writeValueAsString(expectedErrorResponse);

        assertThat(actualResponseBody).isEqualToIgnoringCase(expectedResponseBody);
    }
}