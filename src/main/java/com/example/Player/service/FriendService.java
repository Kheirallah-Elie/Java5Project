package com.example.Player.service;

import com.example.Player.dao.FriendDAO;
import com.example.Player.dao.PlayerDAO;
import com.example.Player.dto.PlayerWithFriendsDTO;
import com.example.Player.model.Friend;
import com.example.Player.model.Player;
import com.example.Player.repository.IFriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    /*

    // Create a new friend
    // Adds a new friend to the specified player
    public Friend addFriendToPlayer(long playerId, long friendId) {
        Player player = playerDAO.findPlayerById(playerId);
        Player friendPlayer = playerDAO.findPlayerById(friendId);

        if (player == null) {
            throw new RuntimeException("Player with ID " + playerId + " not found.");
        }
        if (friendPlayer == null) {
            throw new RuntimeException("Friend with ID " + friendId + " not found.");
        }

        Friend friend = new Friend();
        friend.setPlayer(player);
        friend.setFriendID((int) friendId);

        return friendRepo.save(friend);  // Persist friend
    }

    public Friend updateFriendByPlayerId(long playerId, Friend updatedFriend) {
        Optional<Friend> existingFriend = friendRepo.findById(playerId);
        if (existingFriend.isPresent()) {
            Friend current = existingFriend.get();
            current.setFriendID(updatedFriend.getFriendID());
            current.setPlayer(updatedFriend.getPlayer());
            return friendRepo.save(current);
        }
        throw new RuntimeException("Friend not found.");
    }



    */
    // Retrieve all players with their friends
    public List<PlayerWithFriendsDTO> getAllPlayersWithFriends() {
        return friendDAO.finAllPlayersWithAllTheirFriends();
    }

    public List<PlayerWithFriendsDTO> getFriendsByPlayerId(long playerId) {
        return friendDAO.findFriendsByPlayerId(playerId);
    }

    /*
    // Retrieve a friend by ID
    public Optional<Friend> getFriendById(long id) {
        return friendRepo.findById(id);
    }

    // Update a friend by ID
    public Friend updateFriend(long id, Friend friend) {
        Optional<Friend> existingFriend = friendRepo.findById(id);
        if (existingFriend.isPresent()) {
            Friend updatedFriend = existingFriend.get();
            updatedFriend.setFriendID(friend.getFriendID());
            updatedFriend.setPlayer(friend.getPlayer());
            return friendRepo.save(updatedFriend);
        }
        throw new RuntimeException("Friend not found with id " + id);
    }

    // Delete a friend by ID
    public String deleteFriend(long id) {
        friendRepo.deleteById(id);
        return "Friend with ID " + id + " has been deleted!";
    }
    public void deleteFriendByPlayerId(long playerId, long friendId) {
        Friend friend = friendDAO.findFriendById(playerId);
        if (friend == null || friend.getFriendID() != friendId) {
            throw new RuntimeException("Friend not found with player ID " + playerId);
        }
        friendRepo.deleteById(playerId);
    }*/

}
