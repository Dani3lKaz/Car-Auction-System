package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.dto.VehicleDto;
import com.kazmierczak.daniel.car_auction_platform.security.JwtService;
import com.kazmierczak.daniel.car_auction_platform.service.VehicleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VehicleRestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class VehicleRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VehicleService vehicleService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    @DisplayName("Should return 200 OK and list of vehicles when requested for all vehicles")
    void shouldReturnAllVehicles() throws Exception {
        VehicleDto v1 = VehicleDto.builder().id(1L).brand("ABC").build();
        VehicleDto v2 = VehicleDto.builder().id(2L).brand("DEF").build();
        when(vehicleService.getAll()).thenReturn(List.of(v1, v2));

        mockMvc.perform(get("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].brand").value("ABC"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].brand").value("DEF"));
    }

    @Test
    @DisplayName("Should return 200 OK and vehicle data when requested by ID")
    void shouldReturnVehicleById() throws Exception{
        VehicleDto mockDto = VehicleDto.builder()
                .id(1L)
                .vin("VIN123")
                .brand("ABC")
                .build();

        when(vehicleService.getById(1L)).thenReturn(mockDto);

        mockMvc.perform(get("/api/vehicles/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.vin").value("VIN123"))
                .andExpect(jsonPath("$.brand").value("ABC"));
    }

    @Test
    @DisplayName("Should return 201 CREATED and vehicle data when saved vehicle")
    void shouldReturnedSavedVehicle() throws Exception {
        VehicleDto inputDto = VehicleDto.builder()
                .vin("VIN123")
                .brand("ABC")
                .build();

        VehicleDto outputDto = VehicleDto.builder()
                .id(1L)
                .vin("VIN123")
                .brand("ABC")
                .build();

        when(vehicleService.saveVehicle(any(VehicleDto.class))).thenReturn(outputDto);

        String requestBody = objectMapper.writeValueAsString(inputDto);

        mockMvc.perform(post("/api/vehicles").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.vin").value("VIN123"))
                .andExpect(jsonPath("$.brand").value("ABC"));
    }

    @Test
    @DisplayName("Should return 200 OK and vehicle data when updated")
    void shouldReturnedUpdatedVehicle() throws  Exception {
        VehicleDto mockDto = VehicleDto.builder()
                .id(1L)
                .vin("VIN123")
                .brand("ABC")
                .build();

        when(vehicleService.saveVehicle(mockDto)).thenReturn(mockDto);

        String requestBody = objectMapper.writeValueAsString(mockDto);

        mockMvc.perform(put("/api/vehicles").contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.vin").value("VIN123"))
                .andExpect(jsonPath("$.brand").value("ABC"));
    }

    @Test
    @DisplayName("Should return 200 OK and deletion message when vehicle is deleted")
    void shouldDeleteVehicle() throws Exception {
        long vehicleId = 1L;
        doNothing().when(vehicleService).deleteById(vehicleId);

        mockMvc.perform(delete("/api/vehicles/{id}", vehicleId))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted vehicle id - 1"));
    }
}
