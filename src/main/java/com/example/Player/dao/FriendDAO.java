package com.example.Player.dao;

import com.example.Player.dto.FriendDTO;
import com.example.Player.dto.PlayerWithFriendsDTO;
import com.example.Player.repository.IFriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FriendDAO {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private IFriendRepository friendRepo;

    // No CRUD necessary here as we are using PlayerDAO to add and remove friends

    // Queries to show all the list of players with friends for diagnosis, not asked
    public List<PlayerWithFriendsDTO> getFriendsByPlayerId(long playerId) {
        List<Object[]> rawResults = friendRepo.findRawFriendsByPlayerId(playerId);
        return mapResultsToDTO(rawResults);
    }

    public List<PlayerWithFriendsDTO> getAllPlayersWithFriends() {
        List<Object[]> rawResults = friendRepo.findRawAllPlayersWithAllTheirFriends();
        return mapResultsToDTO(rawResults);
    }

    private List<PlayerWithFriendsDTO> mapResultsToDTO(List<Object[]> results) {
        Map<Integer, PlayerWithFriendsDTO> playerMap = new HashMap<>();

        for (Object[] row : results) {
            int playerId = ((Number) row[0]).intValue();       // Player ID
            String playerName = (String) row[1];               // Player Name
            int friendId = row[2] != null ? ((Number) row[2]).intValue() : 0;   // Friend ID (if exists)
            String friendName = row[4] != null ? (String) row[4] : "";          // Friend Name (if exists)

            // Get the existing PlayerWithFriendsDTO or create a new one if it doesn't exist
            PlayerWithFriendsDTO playerDTO = playerMap.get(playerId);

            if (playerDTO == null) {
                // Create and add new DTO if player not in map yet
                playerDTO = new PlayerWithFriendsDTO(playerId, playerName);
                playerMap.put(playerId, playerDTO);
            }

            // If friend information is available, add it to the player's list of friends
            if (friendId != 0) {
                FriendDTO friendDTO = new FriendDTO((long) friendId, friendName);
                playerDTO.getFriends().add(friendDTO);  // Add the FriendDTO to the player's friend list
            }
        }

        // Return a list of PlayerWithFriendsDTO, each with its corresponding list of friends
        return new ArrayList<>(playerMap.values());
    }
}
