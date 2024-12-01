package com.example.Player.controller;

import com.example.Player.dto.PlayerDTO;
import com.example.Player.model.Player;
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
    public String addPlayer(@RequestBody PlayerDTO playerDTO) {
        Player player = new Player();
        player.setName(playerDTO.getName());
        player.setUsername(playerDTO.getUsername());
        player.setEmail(playerDTO.getEmail());
        player.setLevel(playerDTO.getLevel());
        player.setTotal_points(playerDTO.getTotal_points());
        playerService.addPlayer(player);
        return "Player added successfully!";
    }

    @PutMapping("/update/{id}")
    public String updatePlayer(@PathVariable long id, @RequestBody PlayerDTO playerDTO) {
        Player player = playerService.findPlayerById(id);
        player.setName(playerDTO.getName());
        player.setUsername(playerDTO.getUsername());
        player.setEmail(playerDTO.getEmail());
        player.setLevel(playerDTO.getLevel());
        player.setTotal_points(playerDTO.getTotal_points());
        playerService.updatePlayer(id, player);
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

    @GetMapping("/{id}")
    public Optional<PlayerDTO> getPlayerById(@PathVariable long id) {
        return playerService.getPlayerById(id);
    }

}
