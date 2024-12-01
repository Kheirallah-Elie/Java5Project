package com.example.Player.dto;

import lombok.Data;

@Data
public class FriendDTO {
    private int friendID;
    private String friendName;

    public FriendDTO(int friendID, String friendName) {
        this.friendID = friendID;
        this.friendName = friendName;
    }
}
