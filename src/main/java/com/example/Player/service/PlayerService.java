package com.example.Player.service;

import com.example.Player.dao.PlayerDAO;
import com.example.Player.dto.PlayerAddDTO;
import com.example.Player.dto.PlayerDTO;
import com.example.Player.model.Player;
import com.example.Player.repository.IPlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    @Autowired
    private PlayerDAO playerDAO;
    @Autowired
    private IPlayerRepository playerRepository;

    @Transactional
    public void addPlayer(PlayerAddDTO playerAddDTO) { // I decided to add players and restrict adding total points and level
        Player player = new Player();
        player.setName(playerAddDTO.getName());
        player.setUsername(playerAddDTO.getUsername());
        player.setEmail(playerAddDTO.getEmail());
        playerDAO.addPlayer(player);
    }

    @Transactional
    public void updatePlayer(long playerId, PlayerDTO playerDTO) { // updating the player can use a different DTO that accessed their level and total points
        Player player = findPlayerById(playerId);
        player.setName(playerDTO.getName());
        player.setUsername(playerDTO.getUsername());
        player.setEmail(playerDTO.getEmail());
        player.setLevel(playerDTO.getLevel());
        player.setTotal_points(playerDTO.getTotal_points());
        playerDAO.updatePlayer(playerId, player);
    }

    public void deletePlayer(long playerId) {
        playerDAO.deletePlayer(playerId);
    }

    public Player findPlayerById(long playerId) {
        return playerRepository.findById(playerId).orElse(null);
    }

    // Retrieve all players
    public List<PlayerDTO> getAllPlayers() {
        List<Player> players = playerRepository.findAll();
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