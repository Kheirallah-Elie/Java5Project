package com.example.Player.service;

import com.example.Player.dao.FriendDAO;
import com.example.Player.dao.PlayerDAO;
import com.example.Player.dto.PlayerWithFriendsDTO;

import com.example.Player.model.Player;
import com.example.Player.repository.IFriendRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {

    @Autowired
    private IFriendRepository friendRepo;
    @Autowired
    private FriendDAO friendDAO;
    @Autowired
    private PlayerDAO playerDAO;

    @Transactional
    public void addFriendToPlayer(long playerId, long friendId) {
        //friendDAO.addFriendToPlayer(playerId, friendId);
        Player player = playerDAO.findPlayerById(playerId);  // Fetch the player entity
        Player friend = playerDAO.findPlayerById(friendId);  // Fetch the friend entity

        if (player != null && friend != null) {
            player.addFriend(friend);  // Add friend in both directions
            playerDAO.addPlayer(player);    // Save the player entity, JPA will cascade
        }
    }

    @Transactional
    public void deleteFriendByPlayerId(long playerId, long friendId) {
        //friendDAO.deleteFriendByPlayerId(playerId, friendId);
        Player player = playerDAO.findPlayerById(playerId);
        Player friend = playerDAO.findPlayerById(friendId);

        if (player != null && friend != null) {
            player.removeFriend(friendId);  // Remove the friend from player
            friend.removeFriend(playerId);  // Remove the reverse relationship
            playerDAO.savePlayer(player);   // Save the updated player
            playerDAO.savePlayer(friend);   // Save the updated friend
        }
    }
    // Retrieve all players with their friends
    public List<PlayerWithFriendsDTO> getAllPlayersWithFriends() {
        return friendDAO.getAllPlayersWithFriends();
    }

    public List<PlayerWithFriendsDTO> getFriendsByPlayerId(long playerId) {
        return friendDAO.getFriendsByPlayerId(playerId);
    }

}
