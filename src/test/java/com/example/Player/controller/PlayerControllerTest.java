package com.example.Player.controller;

import com.example.Player.dto.PlayerAddDTO;
import com.example.Player.dto.PlayerDTO;
import com.example.Player.service.impl.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlayerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    public PlayerControllerTest() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(playerController).build();
    }

    @Test
    public void testAddPlayer() throws Exception {
        PlayerAddDTO playerAddDTO = new PlayerAddDTO();
        playerAddDTO.setName("John");
        playerAddDTO.setUsername("john_doe");
        playerAddDTO.setEmail("john@example.com");

        doNothing().when(playerService).addPlayer(any(PlayerAddDTO.class));

        mockMvc.perform(post("/players/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"John\",\"username\":\"john_doe\",\"email\":\"john@example.com\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Player added successfully!"));

        verify(playerService, times(1)).addPlayer(any(PlayerAddDTO.class));
    }

    @Test
    public void testUpdatePlayer() throws Exception {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setName("Updated Name");

        doNothing().when(playerService).updatePlayer(eq(1L), any(PlayerDTO.class));

        mockMvc.perform(put("/players/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"name\":\"Updated Name\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Player updated successfully!"));

        verify(playerService, times(1)).updatePlayer(eq(1L), any(PlayerDTO.class));
    }

    @Test
    public void testDeletePlayer() throws Exception {
        doNothing().when(playerService).deletePlayer(1L);

        mockMvc.perform(delete("/players/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Player deleted successfully!"));

        verify(playerService, times(1)).deletePlayer(1L);
    }


    @Test
    public void testGetPlayerById() throws Exception {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setId(1L);
        playerDTO.setName("Player Name");

        when(playerService.getPlayerByIdToPlayerDTO(1L)).thenReturn(Optional.of(playerDTO));

        mockMvc.perform(get("/players/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Player Name"));

        verify(playerService, times(1)).getPlayerByIdToPlayerDTO(1L);
    }

    @Test
    public void testUpdatePlayerPoints() throws Exception {
        doNothing().when(playerService).updatePlayerPoints(1L, 50);

        mockMvc.perform(post("/players/1/updatePoints/50"))
                .andExpect(status().isOk())
                .andExpect(content().string("50 added to player 1"));

        verify(playerService, times(1)).updatePlayerPoints(1L, 50);
    }

    @Test
    public void testAddAttendanceToPlayer() throws Exception {
        doNothing().when(playerService).addAttendanceToPlayer(1L, 100L);

        mockMvc.perform(post("/players/1/addAttendance/100"))
                .andExpect(status().isOk());

        verify(playerService, times(1)).addAttendanceToPlayer(1L, 100L);
    }
}