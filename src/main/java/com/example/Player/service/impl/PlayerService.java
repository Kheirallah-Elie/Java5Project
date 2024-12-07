package com.example.Player.service.impl;

import com.example.Player.dao.impl.PlayerDAO;
import com.example.Player.dto.PlayerAddDTO;
import com.example.Player.dto.PlayerDTO;
import com.example.Player.model.Player;
import com.example.Player.repository.IPlayerRepository;
import com.example.Player.service.IPlayerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService implements IPlayerService {
    @Autowired
    private PlayerDAO playerDAO;
    @Autowired
    private IPlayerRepository playerRepository; // used for testing, not needed here

    @Override
    @Transactional
    public void addPlayer(PlayerAddDTO playerAddDTO) { // I decided to add players and restrict adding total points and level at first through the PlayerAddDTO
        Player player = new Player();
        player.setName(playerAddDTO.getName());
        player.setUsername(playerAddDTO.getUsername());
        player.setEmail(playerAddDTO.getEmail());
        playerDAO.addPlayer(player);
    }
    @Override
    public Player getPlayerById(long playerId) {
        return playerDAO.getPlayerById(playerId);
    }
    @Override
    @Transactional
    public void updatePlayer(long playerId, PlayerDTO playerDTO) { // updating the player can use a different DTO that accessed their level and total points
        Player player = getPlayerById(playerId);
        player.setName(playerDTO.getName());
        player.setUsername(playerDTO.getUsername());
        player.setEmail(playerDTO.getEmail());
        player.setLevel(playerDTO.getLevel());
        player.setTotal_points(playerDTO.getTotal_points());
        player.setAttendanceIDs(playerDTO.getAttendanceIDs());
        playerDAO.updatePlayer(playerId, player);
    }
    @Override
    public void deletePlayer(long playerId) {
        playerDAO.deletePlayer(playerId);
    }



    // This will receive communication from the Game app to update the player's points, external to the interface
    @Transactional
    public void updatePlayerPoints(long playerId, int points) {
        Player player = getPlayerById(playerId);
        if (player != null) {
            player.setTotal_points(player.getTotal_points() + points);  // Add points to total
            playerDAO.updatePlayer(playerId, player);
        } else {
            throw new RuntimeException("Player not found with id: " + playerId);
        }
    }
    public void addAttendanceToPlayer(long playerId, long attendanceId) {
        Player player = getPlayerById(playerId);  // Fetch the player by ID
        if (player != null) {
            List<Long> attendanceIDs = player.getAttendanceIDs();  // Get the current list of attendance IDs

            if (attendanceIDs != null) {
                attendanceIDs.add(attendanceId);  // Add the new attendance ID
            } else {
                attendanceIDs = new ArrayList<>();  // Initialize a new list if null
                attendanceIDs.add(attendanceId);
            }

            player.setAttendanceIDs(attendanceIDs);  // Set the updated list back to the player
            playerDAO.updatePlayer(playerId, player);  // Update the player in the database
        }
    }

    /////  **** THIS IS OPTIONAL FOR TESTING **** /////
    // Retrieve all players
    public List<PlayerDTO> getAllPlayers() {
        List<Player> players = playerRepository.findAll(); // using the repository directly here for testing, only have a single player Get in the structure
        return players.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    // Retrieve a player by ID to a PlayerDTO, not a Player Object
    public Optional<PlayerDTO> getPlayerByIdToPlayerDTO(long id) {
        Player player = playerDAO.getPlayerById(id);
        return player != null ? Optional.of(convertToDTO(player)) : Optional.empty();
    }
    /////  **** THIS IS OPTIONAL FOR TESTING **** /////

    // Method to convert a Player object to PlayerDTO
    private PlayerDTO convertToDTO(Player player) {
        PlayerDTO dto = new PlayerDTO();
        dto.setId(player.getId());
        dto.setName(player.getName());
        dto.setUsername(player.getUsername());
        dto.setEmail(player.getEmail());
        dto.setLevel(player.getLevel());
        dto.setTotal_points(player.getTotal_points());
        dto.setAttendanceIDs(player.getAttendanceIDs());
        return dto;
    }
}