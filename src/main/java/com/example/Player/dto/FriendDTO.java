package com.example.Player.dto;

import lombok.Data;

@Data
public class FriendDTO {
    private Long friendId;
    private String friendName;

    public FriendDTO(Long friendId, String friendName) {
        this.friendId = friendId;
        this.friendName = friendName;
    }
}