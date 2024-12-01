package com.example.Player.dao;

import com.example.Player.dto.FriendDTO;
import com.example.Player.dto.PlayerWithFriendsDTO;
import com.example.Player.model.Friend;
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

    public List<PlayerWithFriendsDTO> findAllPlayersWithFriends() {
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

    private List<PlayerWithFriendsDTO> mapResultsToDTO(List<Object[]> results) {
        Map<Integer, PlayerWithFriendsDTO> playerMap = new HashMap<>();

        for (Object[] row : results) {
            int playerId = ((Number) row[0]).intValue();  // Cast player ID to int
            String playerName = (String) row[1];
            int friendId = row[2] != null ? ((Number) row[2]).intValue() : 0;  // Cast friend ID to int or 0 if null
            String friendName = row[4] != null ? (String) row[4] : "";  // Get friend name or empty if null

            PlayerWithFriendsDTO playerDTO = playerMap.get(playerId);

            if (playerDTO == null) {
                // If player not already in map, create and add a new DTO with no friends initially
                playerDTO = new PlayerWithFriendsDTO(playerId, playerName);
                playerMap.put(playerId, playerDTO);
            }

            // If friend information is available, create a FriendDTO and add it to the DTO's friends list
            if (friendId != 0) {
                FriendDTO friendDTO = new FriendDTO(friendId, friendName);
                playerDTO.getFriends().add(friendDTO);  // Add the FriendDTO object to the player's friends list
            }
        }

        return new ArrayList<>(playerMap.values());
    }



    public Friend findFriendById(int id) {
        return entityManager.find(Friend.class, id);
    }

    public Friend saveFriend(Friend friend) {
        entityManager.persist(friend);
        return friend;
    }

    public Friend updateFriend(Friend friend) {
        return entityManager.merge(friend);
    }

    public void deleteFriend(int id) {
        Friend friend = findFriendById(id);
        if (friend != null) {
            entityManager.remove(friend);
        }
    }
}
