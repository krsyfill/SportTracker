package com.example.sporttracker.repository;

import com.example.sporttracker.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPlayer_Id(Long playerId);
}
