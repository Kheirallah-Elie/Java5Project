package com.example.Player.service;

import com.example.Player.dao.PlayerDAO;
import com.example.Player.dto.PlayerDTO;
import com.example.Player.model.Player;
import com.example.Player.repository.IPlayerRepository;
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

    public void addPlayer(PlayerDTO playerDTO) {
        Player player = new Player();
        setPlayer(playerDTO, player);
        playerDAO.addPlayer(setPlayer(playerDTO, player));
    }

    public void updatePlayer(long playerId, PlayerDTO playerDTO) {
        Player player = findPlayerById(playerId);
        playerDAO.updatePlayer(playerId, setPlayer(playerDTO, player));
    }

    public void deletePlayer(long playerId) {
        playerDAO.deletePlayer(playerId);
    }

    public Player findPlayerById(long playerId) {
        return playerRepository.findById(playerId).orElse(null);
        //return playerDAO.findPlayerById(playerId);
    }

    // Retrieve all players
    public List<PlayerDTO> getAllPlayers() {
        List<Player> players = playerRepository.findAll();
        return players.stream().map(this::convertToDTO).collect(Collectors.toList());
        //List<Player> players = playerDAO.findAllPlayers();
        //return players.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    // Retrieve a player by ID
    public Optional<PlayerDTO> getPlayerById(long id) {
        Player player = playerDAO.findPlayerById(id);
        return player != null ? Optional.of(convertToDTO(player)) : Optional.empty();
    }

    // Creating methods to avoid code duplication
    public Player setPlayer(PlayerDTO playerDTO, Player player){
        player.setName(playerDTO.getName());
        player.setUsername(playerDTO.getUsername());
        player.setEmail(playerDTO.getEmail());
        player.setLevel(playerDTO.getLevel());
        player.setTotal_points(playerDTO.getTotal_points());
        return player;
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