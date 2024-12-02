package com.example.Player.service;

import com.example.Player.dao.PlayerDAO;
import com.example.Player.dto.PlayerDTO;
import com.example.Player.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    @Autowired
    private PlayerDAO playerDAO;


    public void addPlayer(Player player) {
        playerDAO.addPlayer(player);
    }

    public void updatePlayer(long playerId, Player player) {
        playerDAO.updatePlayer(playerId, player);
    }

    public void deletePlayer(long playerId) {
        playerDAO.deletePlayer(playerId);
    }

    public Player findPlayerById(long playerId) {
        return playerDAO.findPlayerById(playerId);
    }

    // Retrieve all players
    public List<PlayerDTO> getAllPlayers() {
        List<Player> players = playerDAO.findAllPlayers();
        return players.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    // Retrieve a player by ID
    public Optional<PlayerDTO> getPlayerById(long id) {
        Player player = playerDAO.findPlayerById(id);
        return player != null ? Optional.of(convertToDTO(player)) : Optional.empty();
    }
    private PlayerDTO convertToDTO(Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setName(player.getName());
        dto.setUsername(player.getUsername());
        dto.setEmail(player.getEmail());
        dto.setLevel(player.getLevel());
        dto.setTotal_points(player.getTotal_points());
        return dto;
    }
}
