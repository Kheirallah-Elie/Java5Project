package com.example.Player.service.impl;

import com.example.Player.dao.impl.FriendDAO;
import com.example.Player.dao.impl.PlayerDAO;
import com.example.Player.dto.PlayerWithFriendsDTO;

import com.example.Player.model.Player;
import com.example.Player.service.IFriendService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService implements IFriendService {
    @Autowired
    private FriendDAO friendDAO;
    @Autowired
    private PlayerDAO playerDAO;

    @Transactional
    public void addFriend(long playerId, long friendId) {
        Player player = playerDAO.getPlayerById(playerId);  // Fetch the player entity
        Player friend = playerDAO.getPlayerById(friendId);  // Fetch the friend entity

        if (player != null && friend != null) {
            player.addFriend(friend);  // Add friend in both directions
            playerDAO.addPlayer(player);    // Save the player entity, JPA will cascade
        }
    }

    @Transactional
    public void deleteFriend(long playerId, long friendId) {
        Player player = playerDAO.getPlayerById(playerId);
        Player friend = playerDAO.getPlayerById(friendId);

        if (player != null && friend != null) {
            player.removeFriend(friendId);  // Remove the friend from player
            friend.removeFriend(playerId);  // Remove the reverse relationship
            playerDAO.savePlayer(player);   // Save the updated player
            playerDAO.savePlayer(friend);   // Save the updated friend
        }
    }



    // Optional methods: Retrieve all players with their friends // not included in the interface
    public List<PlayerWithFriendsDTO> getAllPlayersWithFriends() {
        return friendDAO.getAllPlayersWithFriends();
    }

    public List<PlayerWithFriendsDTO> getFriendsByPlayerId(long playerId) {
        return friendDAO.getFriendsByPlayerId(playerId);
    }
}