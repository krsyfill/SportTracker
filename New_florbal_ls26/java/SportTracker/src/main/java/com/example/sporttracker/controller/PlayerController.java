package com.example.sporttracker.controller;

import com.example.sporttracker.entity.Player;
import com.example.sporttracker.repository.PlayerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public List<Player> getPlayers() {

        return playerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Player getPlayerById(
            @PathVariable Long id
    ) {

        return playerRepository.findById(id)
                .orElse(null);
    }

    @PutMapping("/{id}")
    public Player updatePlayer(
            @PathVariable Long id,
            @RequestBody Player updatedPlayer
    ) {

        Player player =
                playerRepository.findById(id)
                        .orElse(null);

        if (player == null) {
            return null;
        }

        player.setFirstName(
                updatedPlayer.getFirstName()
        );

        player.setLastName(
                updatedPlayer.getLastName()
        );

        player.setPhone(
                updatedPlayer.getPhone()
        );

        player.setBirthDate(
                updatedPlayer.getBirthDate()
        );

        player.setNote(
                updatedPlayer.getNote()
        );

        return playerRepository.save(player);
    }
}