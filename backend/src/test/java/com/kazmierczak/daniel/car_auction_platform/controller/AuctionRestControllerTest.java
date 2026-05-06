package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.dto.AuctionDto;
import com.kazmierczak.daniel.car_auction_platform.service.AuctionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuctionRestController.class)
public class AuctionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuctionService auctionService;

    @Test
    @DisplayName("Should return 200 OK and list of auctions when requested for all auctions")
    void shouldReturnAllAuctions() throws Exception {
        //given
        AuctionDto a1 = AuctionDto.builder()
                .id(1L)
                .startPrice(BigDecimal.valueOf(10000))
                .status("ACTIVE")
                .build();
        AuctionDto a2 = AuctionDto.builder()
                .id(2L)
                .startPrice(BigDecimal.valueOf(50000))
                .status("FINISHED")
                .build();
        when(auctionService.getAll()).thenReturn(List.of(a1, a2));

        //when & then
        mockMvc.perform(get("/api/auctions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].startPrice").value(10000))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].startPrice").value(50000))
                .andExpect(jsonPath("$[1].status").value("FINISHED"));
    }

    @Test
    @DisplayName("Should return 200 OK and auction data when requested by ID")
    void shouldReturnAuctionById() throws Exception {
        //given
        AuctionDto mockDto = AuctionDto.builder()
                .id(1L)
                .startPrice(BigDecimal.valueOf(15000))
                .status("ACTIVE")
                .build();

        when(auctionService.getById(1L)).thenReturn(mockDto);

        //when & then
        mockMvc.perform(get("/api/auctions/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.startPrice").value(15000))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("Should return 201 CREATED and auction data when saved auction")
    void shouldReturnSavedAuction() throws Exception {
        //given
        AuctionDto inputDto = AuctionDto.builder()
                .startPrice(BigDecimal.valueOf(15000))
                .status("ACTIVE")
                .build();

        AuctionDto outputDto = AuctionDto.builder()
                .id(1L)
                .startPrice(BigDecimal.valueOf(15000))
                .status("ACTIVE")
                .build();

        when(auctionService.saveAuction(any(AuctionDto.class))).thenReturn(outputDto);
        String requestBody = objectMapper.writeValueAsString(inputDto);

        //when & then
        mockMvc.perform(post("/api/auctions").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.startPrice").value(15000))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("Should return 200 OK and auction data when updated")
    void shouldReturnUpdatedAuction() throws Exception {
        //given
        AuctionDto mockDto = AuctionDto.builder()
                .id(1L)
                .startPrice(BigDecimal.valueOf(20000))
                .status("FINISHED")
                .build();

        when(auctionService.saveAuction(any(AuctionDto.class))).thenReturn(mockDto);
        String requestBody = objectMapper.writeValueAsString(mockDto);

        //when & then
        mockMvc.perform(put("/api/auctions").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.startPrice").value(20000))
                .andExpect(jsonPath("$.status").value("FINISHED"));
    }

    @Test
    @DisplayName("Should return 200 OK and deletion message when auction is deleted")
    void shouldDeleteAuction() throws Exception {
        //given
        Long auctionId = 1L;
        doNothing().when(auctionService).deleteById(auctionId);

        //when & then
        mockMvc.perform(delete("/api/auctions/{id}", auctionId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted auction id - 1"));
    }
}
