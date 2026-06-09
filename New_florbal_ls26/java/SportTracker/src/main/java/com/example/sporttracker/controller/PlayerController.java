package com.example.sporttracker.controller;

import com.example.sporttracker.entity.Player;
import com.example.sporttracker.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<Player> getPlayers() {

        return playerService.getPlayers();
    }

    @GetMapping("/{id}")
    public Player getPlayerById(
            @PathVariable Long id
    ) {

        return playerService.getPlayerById(id);
    }

    @PutMapping("/{id}")
    public Player updatePlayer(
            @PathVariable Long id,
            @Valid @RequestBody Player updatedPlayer
    ) {

        return playerService.updatePlayer(id, updatedPlayer);
    }

    @GetMapping("/{id}/attendance-rate")
    public double getAttendanceRate(
            @PathVariable Long id
    ) {
        return playerService.getAttendanceRate(id);
    }
}