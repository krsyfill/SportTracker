package com.example.sporttracker.service;

import com.example.sporttracker.entity.Player;
import com.example.sporttracker.exception.PlayerNotFoundException;
import com.example.sporttracker.repository.AttendanceRepository;
import com.example.sporttracker.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final AttendanceRepository attendanceRepository;

    public PlayerService(
            PlayerRepository playerRepository,
            AttendanceRepository attendanceRepository
    ) {
        this.playerRepository = playerRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));
    }

    public Player updatePlayer(Long id, Player updatedPlayer) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));

        player.setFirstName(updatedPlayer.getFirstName());
        player.setLastName(updatedPlayer.getLastName());
        player.setPhone(updatedPlayer.getPhone());
        player.setBirthDate(updatedPlayer.getBirthDate());
        player.setNote(updatedPlayer.getNote());

        return playerRepository.save(player);
    }

    public double getAttendanceRate(Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new PlayerNotFoundException("Player not found");
        }

        long totalAttendance = attendanceRepository.countByPlayer_Id(playerId);

        if (totalAttendance == 0) {
            return 0;
        }

        long actualAttendance = attendanceRepository.countByPlayer_IdAndActualAttendanceTrue(playerId);

        return ((double) actualAttendance / totalAttendance) * 100;
    }
}
