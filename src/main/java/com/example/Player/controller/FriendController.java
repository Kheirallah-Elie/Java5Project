package com.example.Player.controller;

import com.example.Player.dto.PlayerWithFriendsDTO;
import com.example.Player.model.Friend;
import com.example.Player.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/friends")  // Base URL for Friend CRUD operations
public class FriendController {

    @Autowired
    private FriendService friendService;

    // Create a new friend
    @PostMapping("/add")
    public Friend addFriend(@RequestBody Friend friend) {
        return friendService.addFriend(friend);
    }

    // Retrieve all friends (if needed separately)
    @GetMapping("/all")
    public List<Friend> getAllFriends() {
        return friendService.getAllFriends();
    }

    // Retrieve all players with their friends
    @GetMapping("/playersWithFriends")
    public List<PlayerWithFriendsDTO> getAllPlayersWithFriends() {
        return friendService.getAllPlayersWithFriends();
    }

    // Retrieve a friend by ID
    @GetMapping("/{id}")
    public Optional<Friend> getFriendById(@PathVariable long id) {
        return friendService.getFriendById(id);
    }

    // Update a friend by ID
    @PutMapping("/update/{id}")
    public Friend updateFriend(@PathVariable long id, @RequestBody Friend friend) {
        return friendService.updateFriend(id, friend);
    }

    // Delete a friend by ID
    @DeleteMapping("/delete/{id}")
    public String deleteFriend(@PathVariable long id) {
        return friendService.deleteFriend(id);
    }
}
