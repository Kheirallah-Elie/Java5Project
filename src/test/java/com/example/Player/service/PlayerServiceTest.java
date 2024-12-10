package com.example.Player.service;

import com.example.Player.dao.impl.PlayerDAO;
import com.example.Player.dto.PlayerAddDTO;
import com.example.Player.dto.PlayerDTO;
import com.example.Player.model.Player;
import com.example.Player.service.impl.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    @Mock
    private PlayerDAO playerDAO;
    @InjectMocks
    private PlayerService playerService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPlayer() {
        PlayerAddDTO playerAddDTO = new PlayerAddDTO();
        playerAddDTO.setName("John");
        playerAddDTO.setUsername("john_doe");
        playerAddDTO.setEmail("john@example.com");

        doNothing().when(playerDAO).addPlayer(any(Player.class));

        playerService.addPlayer(playerAddDTO);

        verify(playerDAO, times(1)).addPlayer(any(Player.class));
    }

    @Test
    void testUpdatePlayer() {
        long playerId = 1L;
        Player player = new Player();
        player.setId(playerId);

        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setName("Updated Name");
        playerDTO.setUsername("updated_username");
        playerDTO.setEmail("updated@example.com");
        playerDTO.setLevel(5);
        playerDTO.setTotal_points(100);
        playerDTO.setAttendanceIDs(new ArrayList<>());

        when(playerDAO.getPlayerById(playerId)).thenReturn(player);
        doNothing().when(playerDAO).updatePlayer(eq(playerId), any(Player.class));

        playerService.updatePlayer(playerId, playerDTO);

        verify(playerDAO, times(1)).updatePlayer(eq(playerId), any(Player.class));
        assertEquals("Updated Name", player.getName());
        assertEquals("updated_username", player.getUsername());
        assertEquals("updated@example.com", player.getEmail());
    }

    @Test
    void testDeletePlayer() {
        long playerId = 1L;

        doNothing().when(playerDAO).deletePlayer(playerId);

        playerService.deletePlayer(playerId);

        verify(playerDAO, times(1)).deletePlayer(playerId);
    }

    @Test
    void testUpdatePlayerPoints() {
        long playerId = 1L;
        Player player = new Player();
        player.setId(playerId);
        player.setTotal_points(50);

        when(playerDAO.getPlayerById(playerId)).thenReturn(player);
        doNothing().when(playerDAO).updatePlayer(eq(playerId), any(Player.class));

        playerService.updatePlayerPoints(playerId, 30);

        verify(playerDAO, times(1)).updatePlayer(eq(playerId), any(Player.class));
        assertEquals(80, player.getTotal_points());
    }

    @Test
    void testAddAttendanceToPlayer() {
        long playerId = 1L;
        long attendanceId = 100L;
        Player player = new Player();
        player.setId(playerId);
        player.setAttendanceIDs(new ArrayList<>());

        when(playerDAO.getPlayerById(playerId)).thenReturn(player);
        doNothing().when(playerDAO).updatePlayer(eq(playerId), any(Player.class));

        playerService.addAttendanceToPlayer(playerId, attendanceId);

        verify(playerDAO, times(1)).updatePlayer(eq(playerId), any(Player.class));
        assertTrue(player.getAttendanceIDs().contains(attendanceId));
    }


    @Test
    void testGetPlayerByIdToPlayerDTO() {
        long playerId = 1L;
        Player player = new Player();
        player.setId(playerId);
        player.setName("John Doe");

        when(playerDAO.getPlayerById(playerId)).thenReturn(player);

        Optional<PlayerDTO> result = playerService.getPlayerByIdToPlayerDTO(playerId);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(playerDAO, times(1)).getPlayerById(playerId);
    }
}
