package com.example.Player.dto;

import com.example.Player.model.Friend;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PlayerWithFriendsDTO {
    private Long playerId;
    private String playerName;
    private int playerLevel;
    private List<Friend> friends = new ArrayList<>();

    // Constructor accepting a single friend and adding it to the list
    public PlayerWithFriendsDTO(Long playerId, String playerName, int playerLevel, Friend friend) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerLevel = playerLevel;
        if (friend != null) {
            this.friends.add(friend);  // Add single Friend to the list
        }
    }

    // Constructor that initializes with an empty list of friends
    public PlayerWithFriendsDTO(Long playerId, String playerName, int playerLevel) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerLevel = playerLevel;
        this.friends = new ArrayList<>();
    }

}
