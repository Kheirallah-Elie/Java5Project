package com.example.Player.controller;

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
    public PlayerDTO addPlayer(@RequestBody PlayerDTO playerDTO) {
        return playerService.addPlayer(playerDTO);
    }

    @GetMapping("/all")
    public List<PlayerDTO> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/{id}")
    public Optional<PlayerDTO> getPlayerById(@PathVariable long id) {
        return playerService.getPlayerById(id);
    }

    @PutMapping("/update/{id}")
    public PlayerDTO updatePlayer(@PathVariable long id, @RequestBody PlayerDTO playerDTO) {
        return playerService.updatePlayer(id, playerDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deletePlayer(@PathVariable long id) {
        playerService.deletePlayer(id);
    }
}
