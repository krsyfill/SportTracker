package com.example.sporttracker.service;

import com.example.sporttracker.entity.Payment;
import com.example.sporttracker.entity.Player;
import com.example.sporttracker.exception.PlayerNotFoundException;
import com.example.sporttracker.repository.PaymentRepository;
import com.example.sporttracker.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PlayerRepository playerRepository;

    public PaymentService(PaymentRepository paymentRepository, PlayerRepository playerRepository) {
        this.paymentRepository = paymentRepository;
        this.playerRepository = playerRepository;
    }

    public Payment createPayment(Long playerId, Payment payment) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with id: " + playerId));
        
        payment.setPlayer(player);
        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsByPlayerId(Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException("Player not found with id: " + playerId);
        }
        return paymentRepository.findByPlayer_Id(playerId);
    }

    public BigDecimal getPlayerBalance(Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException("Player not found with id: " + playerId);
        }
        
        List<Payment> payments = paymentRepository.findByPlayer_Id(playerId);
        return payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
