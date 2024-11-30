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

    // Add a new player
    public PlayerDTO addPlayer(PlayerDTO playerDTO) {
        Player player = new Player();
        player.setName(playerDTO.getName());
        player.setUsername(playerDTO.getUsername());
        player.setEmail(playerDTO.getEmail());
        player.setLevel(playerDTO.getLevel());
        player.setTotalPoints(playerDTO.getTotalPoints());

        Player savedPlayer = playerDAO.savePlayer(player);
        return convertToDTO(savedPlayer);
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

    // Update a player
    public PlayerDTO updatePlayer(long id, PlayerDTO playerDTO) {
        Player existingPlayer = playerDAO.findPlayerById(id);
        existingPlayer.setName(playerDTO.getName());
        existingPlayer.setUsername(playerDTO.getUsername());
        existingPlayer.setEmail(playerDTO.getEmail());
        existingPlayer.setLevel(playerDTO.getLevel());
        existingPlayer.setTotalPoints(playerDTO.getTotalPoints());

        Player updatedPlayer = playerDAO.updatePlayer(existingPlayer);
        return convertToDTO(updatedPlayer);
    }

    // Delete a player
    public void deletePlayer(long id) {
        playerDAO.deletePlayer(id);
    }

    private PlayerDTO convertToDTO(Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setName(player.getName());
        dto.setUsername(player.getUsername());
        dto.setEmail(player.getEmail());
        dto.setLevel(player.getLevel());
        dto.setTotalPoints(player.getTotalPoints());
        return dto;
    }
}
