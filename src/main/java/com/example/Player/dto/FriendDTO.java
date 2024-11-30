package com.example.Player.dto;

import lombok.Data;

@Data
public class FriendDTO {
    private long id;
    private long playerId;  // The ID of the associated Player
    private int friendID;   // Friend's unique identifier
}
