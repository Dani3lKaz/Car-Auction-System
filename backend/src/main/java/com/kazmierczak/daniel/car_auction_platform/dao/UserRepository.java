package com.kazmierczak.daniel.car_auction_platform.dao;

import com.kazmierczak.daniel.car_auction_platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
