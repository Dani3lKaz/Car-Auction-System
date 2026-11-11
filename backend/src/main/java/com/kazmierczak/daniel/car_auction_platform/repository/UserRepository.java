package com.kazmierczak.daniel.car_auction_platform.repository;

import com.kazmierczak.daniel.car_auction_platform.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("update User u set u.balance = u.balance - :amount where u.id = :id and u.balance >= :amount")
    int debit(@Param("id") Long id, @Param("amount") BigDecimal amount);

    @Modifying
    @Query("update User u set u.balance = u.balance + :amount where u.id = :id")
    int credit(@Param("id") Long id, @Param("amount") BigDecimal amount);
}
