package com.example.sporttracker.service;

import com.example.sporttracker.entity.Payment;
import com.example.sporttracker.entity.Player;
import com.example.sporttracker.exception.PlayerNotFoundException;
import com.example.sporttracker.repository.PaymentRepository;
import com.example.sporttracker.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldCreatePayment() {
        // Given
        Long playerId = 1L;
        Payment payment = new Payment();
        payment.setAmount(new BigDecimal("150.00"));
        Player player = new Player();
        player.setId(playerId);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // When
        Payment createdPayment = paymentService.createPayment(playerId, payment);

        // Then
        assertEquals(payment.getAmount(), createdPayment.getAmount());
        verify(paymentRepository, times(1)).save(payment);
        assertEquals(player, payment.getPlayer());
    }

    @Test
    void shouldReturnPlayerBalance() {
        // Given
        Long playerId = 1L;
        Payment p1 = new Payment();
        p1.setAmount(new BigDecimal("150.00"));
        Payment p2 = new Payment();
        p2.setAmount(new BigDecimal("200.00"));
        List<Payment> payments = Arrays.asList(p1, p2);

        when(playerRepository.existsById(playerId)).thenReturn(true);
        when(paymentRepository.findByPlayer_Id(playerId)).thenReturn(payments);

        // When
        BigDecimal balance = paymentService.getPlayerBalance(playerId);

        // Then
        assertEquals(new BigDecimal("350.00"), balance);
    }

    @Test
    void shouldThrowPlayerNotFoundException() {
        // Given
        Long playerId = 1L;
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PlayerNotFoundException.class, () -> {
            paymentService.createPayment(playerId, new Payment());
        });
    }
}
