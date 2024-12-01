package com.example.Player.dto;

import lombok.Data;

@Data
public class FriendDTO {
    private long friendId;
    private String friendName;

    public FriendDTO(long friendId, String friendName) {
        this.friendId = friendId;
        this.friendName = friendName;
    }
}