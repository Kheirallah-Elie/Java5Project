package com.example.Player.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PlayerWithFriendsDTO {
    private long playerId;
    private String playerName;
    private List<FriendDTO> friends = new ArrayList<>();

    public PlayerWithFriendsDTO(long playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
    }
}
