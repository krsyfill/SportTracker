package com.example.sporttracker.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class EntityLogicTest {

    @Test
    void testPlayerEntity() {
        Player player = new Player();
        player.setFirstName("Jan");
        player.setLastName("Novák");
        
        assertEquals("Jan", player.getFirstName());
        assertEquals("Novák", player.getLastName());
    }

    @Test
    void testTrainingStatus() {
        Training training = new Training();
        assertEquals(TrainingStatus.PLANNED, training.getStatus(), "Default status should be PLANNED");
        
        training.setStatus(TrainingStatus.FINISHED);
        assertEquals(TrainingStatus.FINISHED, training.getStatus());
    }

    @Test
    void testAttendanceAssociation() {
        Player player = new Player();
        Training training = new Training();
        Attendance attendance = new Attendance();
        
        attendance.setPlayer(player);
        attendance.setTraining(training);
        attendance.setPlannedAttendance(true);
        
        assertEquals(player, attendance.getPlayer());
        assertEquals(training, attendance.getTraining());
        assertTrue(attendance.getPlannedAttendance());
    }
}
