package com.kazmierczak.daniel.car_auction_platform.models.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleUserDTO {
    private String firstName;
    private String lastName;
}
