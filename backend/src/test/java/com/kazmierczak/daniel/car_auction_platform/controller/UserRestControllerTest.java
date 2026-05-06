package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.dto.UserDto;
import com.kazmierczak.daniel.car_auction_platform.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserRestController.class)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("Should return 200 OK and list of users when requested for all users")
    void shouldReturnAllUsers() throws Exception{
        //given
        UserDto u1 = UserDto.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .build();
        UserDto u2 = UserDto.builder()
                .id(2L)
                .firstName("Sample")
                .lastName("Dto")
                .build();
        when(userService.getAll()).thenReturn(List.of(u1, u2));

        //when & then
        mockMvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("Test"))
                .andExpect(jsonPath("$[0].lastName").value("User"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("Sample"))
                .andExpect(jsonPath("$[1].lastName").value("Dto"));

    }

    @Test
    @DisplayName("Should return 200 OK and user data when requested by ID")
    void shouldReturnUserById() throws Exception{
        //given
        UserDto mockDto = UserDto.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .build();

        when(userService.getById(1L)).thenReturn(mockDto);

        //when & then
        mockMvc.perform(get("/api/users/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    @Test
    @DisplayName("Should return 201 CREATED and user data when saved user")
    void shouldReturnSavedUser() throws Exception{
        //given
        UserDto inputDto = UserDto.builder()
                .firstName("Test")
                .lastName("User")
                .build();

        UserDto outputDto = UserDto.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .build();

        when(userService.saveUser(any(UserDto.class))).thenReturn(outputDto);
        String requestBody = objectMapper.writeValueAsString(inputDto);

        //when & then
        mockMvc.perform(post("/api/users").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    @Test
    @DisplayName("Should return 200 OK and user data when updated")
    void shouldReturnUpdatedUser() throws Exception{
        //given
        UserDto mockDto = UserDto.builder()
                .id(1L)
                .firstName("Test")
                .lastName("User")
                .build();

        when(userService.saveUser(mockDto)).thenReturn(mockDto);
        String requestBody = objectMapper.writeValueAsString(mockDto);

        //when & then
        mockMvc.perform(put("/api/users").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    @Test
    @DisplayName("Should return 200 OK and deletion message when user is deleted")
    void shouldDeleteUser() throws Exception{
        //given
        Long userId = 1L;
        doNothing().when(userService).deleteById(userId);

        //when & then
        mockMvc.perform(delete("/api/users/{id}", userId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted user id - 1"));
    }

}
