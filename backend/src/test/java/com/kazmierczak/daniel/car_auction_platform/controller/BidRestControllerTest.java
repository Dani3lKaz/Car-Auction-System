package com.kazmierczak.daniel.car_auction_platform.controller;

import com.kazmierczak.daniel.car_auction_platform.dto.BidDto;
import com.kazmierczak.daniel.car_auction_platform.service.BidService;
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

@WebMvcTest(BidRestController.class)
public class BidRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BidService bidService;

    @Test
    @DisplayName("Should return 200 OK and list of bids when requested for all bids")
    void shouldReturnAllBids() throws Exception {
        //given
        BidDto b1 = BidDto.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(1000))
                .build();
        BidDto b2 = BidDto.builder()
                .id(2L)
                .amount(BigDecimal.valueOf(2500))
                .build();
        when(bidService.getAll()).thenReturn(List.of(b1, b2));

        //when & then
        mockMvc.perform(get("/api/bids").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].amount").value(1000))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].amount").value(2500));
    }

    @Test
    @DisplayName("Should return 200 OK and bid data when requested by ID")
    void shouldReturnBidById() throws Exception {
        //given
        BidDto mockDto = BidDto.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(1500))
                .build();

        when(bidService.getById(1L)).thenReturn(mockDto);

        //when & then
        mockMvc.perform(get("/api/bids/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.amount").value(1500));
    }

    @Test
    @DisplayName("Should return 201 CREATED and bid data when saved bid")
    void shouldReturnSavedBid() throws Exception {
        //given
        BidDto inputDto = BidDto.builder()
                .amount(BigDecimal.valueOf(1500))
                .build();

        BidDto outputDto = BidDto.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(1500))
                .build();

        when(bidService.saveBid(any(BidDto.class))).thenReturn(outputDto);
        String requestBody = objectMapper.writeValueAsString(inputDto);

        //when & then
        mockMvc.perform(post("/api/bids").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.amount").value(1500));
    }

    @Test
    @DisplayName("Should return 200 OK and bid data when updated")
    void shouldReturnUpdatedBid() throws Exception {
        //given
        BidDto mockDto = BidDto.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(2000))
                .build();

        when(bidService.saveBid(any(BidDto.class))).thenReturn(mockDto);
        String requestBody = objectMapper.writeValueAsString(mockDto);

        //when & then
        mockMvc.perform(put("/api/bids").content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.amount").value(2000));
    }

    @Test
    @DisplayName("Should return 200 OK and deletion message when bid is deleted")
    void shouldDeleteBid() throws Exception {
        //given
        Long bidId = 1L;
        doNothing().when(bidService).deleteById(bidId);

        //when & then
        mockMvc.perform(delete("/api/bids/{id}", bidId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted bid id - 1"));
    }
}
