package com.example.Player.service;

import com.example.Player.dao.FriendDAO;
import com.example.Player.dao.PlayerDAO;
import com.example.Player.dto.PlayerWithFriendsDTO;

import com.example.Player.repository.IFriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {

    @Autowired
    private IFriendRepository friendRepo;
    @Autowired
    private FriendDAO friendDAO;
    private PlayerDAO playerDAO;

    public void addFriendToPlayer(long playerId, long friendId) {
        // You can add any business logic or validation here
        friendDAO.addFriendToPlayer(playerId, friendId);
    }

    public void updateFriend(long friendId, long newFriendId) {
        friendDAO.updateFriend(friendId, newFriendId);
    }

    public void deleteFriendByPlayerId(long playerId, long friendId) {
        friendDAO.deleteFriendByPlayerId(playerId, friendId);
    }
    // Retrieve all players with their friends
    public List<PlayerWithFriendsDTO> getAllPlayersWithFriends() {
        return friendDAO.finAllPlayersWithAllTheirFriends();
    }

    public List<PlayerWithFriendsDTO> getFriendsByPlayerId(long playerId) {
        return friendDAO.findFriendsByPlayerId(playerId);
    }

}
