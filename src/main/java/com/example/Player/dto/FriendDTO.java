package com.example.Player.dto;

import lombok.Data;

@Data
public class FriendDTO {
    private int friendId;
    private String friendName;

    public FriendDTO(int friendId, String friendName) {
        this.friendId = friendId;
        this.friendName = friendName;
    }
}