package com.kazmierczak.daniel.car_auction_platform.service;

import com.kazmierczak.daniel.car_auction_platform.dto.VehicleDto;
import com.kazmierczak.daniel.car_auction_platform.entity.Vehicle;
import com.kazmierczak.daniel.car_auction_platform.exception.ResourceNotFoundException;
import com.kazmierczak.daniel.car_auction_platform.exception.VehicleVinAlreadyExistsException;
import com.kazmierczak.daniel.car_auction_platform.mapper.VehicleMapper;
import com.kazmierczak.daniel.car_auction_platform.repository.VehicleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Vehicle Service Implementation Test")
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleServiceImpl;

    @Test
    @DisplayName("Should return vehicle when vehicle exists")
    void shouldReturnVehicleWhenVehicleExists() {
        // given
        long vehicleId = 1L;
        Vehicle vehicle = Vehicle.builder()
                .id(vehicleId)
                .vin("VIN123")
                .brand("ABC")
                .model("Model")
                .build();
        VehicleDto expectedDto = VehicleMapper.toDto(vehicle);

        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        // when
        VehicleDto resultDto = vehicleServiceImpl.getById(vehicleId);

        // then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(expectedDto.getId());
        assertThat(resultDto.getVin()).isEqualTo(expectedDto.getVin());
        assertThat(resultDto.getBrand()).isEqualTo(expectedDto.getBrand());
    }

    @Test
    @DisplayName("Should throw exception when vehicle id not found")
    void shouldThrowExceptionWhenVehicleIdNotFound() {
        // given
        long nonExistentId = 99L;
        when(vehicleRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> vehicleServiceImpl.getById(nonExistentId));
        assertThat(exception.getMessage()).isEqualTo("Vehicle with id " + nonExistentId + " not found.");
    }

    @Test
    @DisplayName("Should successfully save vehicle when valid")
    void shouldSaveVehicleSuccessfully() {
        // given
        VehicleDto inputDto = VehicleDto.builder()
                .vin("VIN123")
                .brand("ABC")
                .build();

        Vehicle savedVehicle = Vehicle.builder()
                .id(1L)
                .vin("VIN123")
                .brand("ABC")
                .build();

        when(vehicleRepository.findByVin("VIN123")).thenReturn(Optional.empty());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(savedVehicle);

        // when
        VehicleDto resultDto = vehicleServiceImpl.saveVehicle(inputDto);

        // then
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.getId()).isEqualTo(1L);
        assertThat(resultDto.getVin()).isEqualTo("VIN123");

        verify(vehicleRepository).findByVin("VIN123");
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Should throw exception when saving new vehicle with existing VIN")
    void shouldThrowExceptionWhenSavingVehicleWithExistingVin() {
        // given
        VehicleDto inputDto = VehicleDto.builder()
                .vin("VIN123")
                .brand("ABC")
                .build();

        Vehicle existingVehicle = Vehicle.builder()
                .id(2L)
                .vin("VIN123")
                .build();

        when(vehicleRepository.findByVin("VIN123")).thenReturn(Optional.of(existingVehicle));

        // when & then
        VehicleVinAlreadyExistsException exception = assertThrows(VehicleVinAlreadyExistsException.class,
                () -> vehicleServiceImpl.saveVehicle(inputDto));
        assertThat(exception.getMessage()).isEqualTo("Vehicle with vin VIN123 already exists.");

        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Should successfully delete vehicle when vehicle exists")
    void shouldDeleteVehicleWhenVehicleExists() {
        // given
        long vehicleId = 1L;
        when(vehicleRepository.existsById(vehicleId)).thenReturn(true);

        // when
        vehicleServiceImpl.deleteById(vehicleId);

        // then
        verify(vehicleRepository).existsById(vehicleId);
        verify(vehicleRepository).deleteById(vehicleId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non existing vehicle")
    void shouldThrowExceptionWhenDeletingNonExistingVehicle() {
        // given
        long nonExistentId = 99L;
        when(vehicleRepository.existsById(nonExistentId)).thenReturn(false);

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
                () -> vehicleServiceImpl.deleteById(nonExistentId));
        assertThat(exception.getMessage()).isEqualTo("Cannot delete. Vehicle with id " + nonExistentId + " not found.");

        verify(vehicleRepository, never()).deleteById(nonExistentId);
    }
}
