package com.example.Player.controller;

import com.example.Player.dto.PlayerAddDTO;
import com.example.Player.dto.PlayerDTO;
import com.example.Player.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @PostMapping("/add")
    public String addPlayer(@RequestBody PlayerAddDTO playerAddDTO) {
        playerService.addPlayer(playerAddDTO);
        return "Player added successfully!";
    }

    @PutMapping("/update/{id}")
    public String updatePlayer(@PathVariable long id, @RequestBody PlayerDTO playerDTO) {
        playerService.updatePlayer(id, playerDTO);
        return "Player updated successfully!";
    }

    @DeleteMapping("/delete/{id}")
    public String deletePlayer(@PathVariable long id) {
        playerService.deletePlayer(id);
        return "Player deleted successfully!";
    }

    @GetMapping("/all")
    public List<PlayerDTO> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    // Endpoint to fetch player details by ID
    @GetMapping("/{id}")
    public PlayerDTO getPlayerById(@PathVariable long id) {
        return playerService.getPlayerById(id).orElseThrow(() -> new RuntimeException("Player not found"));
    }

    // Endpoint to update the player's total points
    @PostMapping("/{id}/updatePoints/{points}")
    public void updatePlayerPoints(@PathVariable("id") int playerId, @PathVariable int points) {
        playerService.updatePlayerPoints(playerId, points);
    }



}
