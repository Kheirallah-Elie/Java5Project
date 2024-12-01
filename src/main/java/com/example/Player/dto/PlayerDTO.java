package com.example.Player.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlayerDTO {
    private long id;
    private String name;
    private String username;
    private String email;
    private int level;
    private int total_points;
    private List<Long> friendIds;  // List of IDs of friends
}
