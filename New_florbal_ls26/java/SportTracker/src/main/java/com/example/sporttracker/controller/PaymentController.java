package com.example.sporttracker.controller;

import com.example.sporttracker.entity.Payment;
import com.example.sporttracker.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/players/{playerId}/payments")
    public Payment createPayment(@PathVariable Long playerId, @Valid @RequestBody Payment payment) {
        return paymentService.createPayment(playerId, payment);
    }

    @GetMapping("/players/{playerId}/payments")
    public List<Payment> getPayments(@PathVariable Long playerId) {
        return paymentService.getPaymentsByPlayerId(playerId);
    }

    @GetMapping("/players/{playerId}/balance")
    public Map<String, Object> getPlayerBalance(@PathVariable Long playerId) {
        BigDecimal balance = paymentService.getPlayerBalance(playerId);
        Map<String, Object> response = new HashMap<>();
        response.put("playerId", playerId);
        response.put("balance", balance);
        return response;
    }
}
