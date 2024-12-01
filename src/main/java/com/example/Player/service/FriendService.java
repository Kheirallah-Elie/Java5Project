package com.example.Player.service;

import com.example.Player.dao.FriendDAO;
import com.example.Player.dto.PlayerWithFriendsDTO;
import com.example.Player.model.Friend;
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

    // Create a new friend
    public Friend addFriend(Friend friend) {
        return friendRepo.save(friend);
    }

    // Retrieve all friends (if needed separately)
    public List<Friend> getAllFriends() {
        return friendRepo.findAll();
    }

    // Retrieve all players with their friends
    public List<PlayerWithFriendsDTO> getAllPlayersWithFriends() {
        return friendDAO.finAllPlayersWithAllTheirFriends();
    }

    public List<PlayerWithFriendsDTO> getFriendsByPlayerId(int playerId) {
        return friendDAO.findFriendsByPlayerId(playerId);
    }


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
}
