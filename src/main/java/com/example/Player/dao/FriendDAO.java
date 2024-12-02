package com.example.Player.dao;

import com.example.Player.dto.FriendDTO;
import com.example.Player.dto.PlayerWithFriendsDTO;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void addFriendToPlayer(long playerId, long friendId) {
        String sqlQuery = """
            INSERT INTO friend (player_id, friendID)
            VALUES (:playerId, :friendId)
        """;

        Query query1 = entityManager.createNativeQuery(sqlQuery);
        query1.setParameter("playerId", playerId);
        query1.setParameter("friendId", friendId);
        query1.executeUpdate();

        // Insert the reverse relationship (friend -> player)
        Query query2 = entityManager.createNativeQuery(sqlQuery);
        query2.setParameter("playerId", friendId);
        query2.setParameter("friendId", playerId);
        query2.executeUpdate();
    }


    @Transactional
    public void updateFriend(long friendId, long newFriendId) {
        String sqlQuery = """
            UPDATE friend
            SET friendID = :newFriendId
            WHERE friendID = :friendId
        """;

        // Update the original relationship
        Query query1 = entityManager.createNativeQuery(sqlQuery);
        query1.setParameter("newFriendId", newFriendId);
        query1.setParameter("friendId", friendId);
        query1.executeUpdate();

        // Update the reverse relationship
        Query query2 = entityManager.createNativeQuery(sqlQuery);
        query2.setParameter("newFriendId", friendId);  // reverse the new and old values
        query2.setParameter("friendId", newFriendId);
        query2.executeUpdate();
    }


    @Transactional
    public void deleteFriendByPlayerId(long playerId, long friendId) {
        String sqlQuery = """
            DELETE FROM friend
            WHERE player_id = :playerId
            AND friendID = :friendId
        """;

        // Delete the original relationship
        Query query1 = entityManager.createNativeQuery(sqlQuery);
        query1.setParameter("playerId", playerId);
        query1.setParameter("friendId", friendId);
        query1.executeUpdate();

        // Delete the reverse relationship
        Query query2 = entityManager.createNativeQuery(sqlQuery);
        query2.setParameter("playerId", friendId);
        query2.setParameter("friendId", playerId);
        query2.executeUpdate();
    }


    // Queries to show all the list of players with friends for diagnosis, not asked
    public List<PlayerWithFriendsDTO> finAllPlayersWithAllTheirFriends() {
        String sqlQuery = """
            SELECT p.id AS player_id, p.name AS player_name, 
                   f.id AS friend_id, f.friendid AS friend_player_id, fp.name AS friend_name
            FROM player p
            LEFT JOIN friend f ON p.id = f.player_id
            LEFT JOIN player fp ON f.friendID = fp.id
            ORDER BY p.id
        """;


        Query query = entityManager.createNativeQuery(sqlQuery);
        List<Object[]> results = query.getResultList();

        // Convert raw SQL result to PlayerWithFriendsDTO
        return mapResultsToDTO(results);
    }

    public List<PlayerWithFriendsDTO> findFriendsByPlayerId(long playerId) {
        String sqlQuery = """
            SELECT p.id AS player_id,
                   p.name AS player_name,
                   f.id AS friend_id,
                   f.friendID AS friend_player_id,
                   fp.name AS friend_name
            FROM player p
            LEFT JOIN friend f ON p.id = f.player_id
            LEFT JOIN player fp ON f.friendID = fp.id
            WHERE p.id = :playerId
            ORDER BY p.id
        """;

        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("playerId", playerId);  // Bind the playerId parameter
        List<Object[]> results = query.getResultList();

        // Convert raw SQL result to PlayerWithFriendsDTO
        return mapResultsToDTO(results);
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
