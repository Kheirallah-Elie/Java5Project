package com.example.Player.dao;

import com.example.Player.dto.PlayerWithFriendsDTO;
import com.example.Player.model.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FriendDAO {

    @Autowired
    private EntityManager entityManager;

    public List<PlayerWithFriendsDTO> findAllPlayersWithFriends() {
        String query = """
        SELECT new com.example.Player.dto.PlayerWithFriendsDTO(p.id, p.name, p.level, f)
        FROM Player p
        LEFT JOIN Friend f ON p.id = f.player.id
        ORDER BY p.id
    """;

        List<PlayerWithFriendsDTO> resultList = entityManager.createQuery(query, PlayerWithFriendsDTO.class).getResultList();

        // Group friends by player and eliminate duplicate Player entries
        return groupFriendsByPlayer(resultList);
    }

    private List<PlayerWithFriendsDTO> groupFriendsByPlayer(List<PlayerWithFriendsDTO> flatList) {
        Map<Long, PlayerWithFriendsDTO> playerMap = new HashMap<>();

        for (PlayerWithFriendsDTO dto : flatList) {
            // Check if PlayerWithFriendsDTO already exists for this player
            if (!playerMap.containsKey(dto.getPlayerId())) {
                // Create a new DTO with an empty list of friends
                playerMap.put(dto.getPlayerId(), new PlayerWithFriendsDTO(dto.getPlayerId(), dto.getPlayerName(), dto.getPlayerLevel()));
            }

            // Add the current Friend to the list if it exists
            if (!dto.getFriends().isEmpty()) {
                Friend friend = dto.getFriends().get(0); // Extract the single friend
                playerMap.get(dto.getPlayerId()).getFriends().add(friend);
            }
        }

        return new ArrayList<>(playerMap.values());
    }



    public Friend findFriendById(long id) {
        return entityManager.find(Friend.class, id);
    }

    public Friend saveFriend(Friend friend) {
        entityManager.persist(friend);
        return friend;
    }

    public Friend updateFriend(Friend friend) {
        return entityManager.merge(friend);
    }

    public void deleteFriend(long id) {
        Friend friend = findFriendById(id);
        if (friend != null) {
            entityManager.remove(friend);
        }
    }
}
