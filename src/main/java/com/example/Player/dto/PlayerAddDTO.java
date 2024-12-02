package com.example.Player.dto;

import lombok.Data;

@Data
public class PlayerAddDTO { // without adding score or total points
    private long id;
    private String name;
    private String username;
    private String email;
}
