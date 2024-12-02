package com.example.Player.controller;

import com.example.Player.dto.PlayerWithFriendsDTO;
import com.example.Player.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")  // Base URL for Friend CRUD operations
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/{playerId}/addFriend/{friendId}")
    public String addFriend(@PathVariable long playerId, @PathVariable long friendId) {
        friendService.addFriendToPlayer(playerId, friendId);
        return "Friend added successfully!";
    }

    @DeleteMapping("/delete/{playerId}/{friendId}")
    public String deleteFriend(@PathVariable long playerId, @PathVariable long friendId) {
        friendService.deleteFriendByPlayerId(playerId, friendId);
        return "Friend deleted successfully!";
    }

    // Retrieve all players with their friends
    @GetMapping("/playersWithFriends")
    public List<PlayerWithFriendsDTO> getAllPlayersWithFriends() {
        return friendService.getAllPlayersWithFriends();
    }

    @GetMapping("/player/{id}/friends")
    public List<PlayerWithFriendsDTO> getFriendsByPlayerId(@PathVariable long id) {
        return friendService.getFriendsByPlayerId(id);
    }
}
