package com.example.Player.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlayerDTO { // complete DTO for updating players
    private long id;
    private String name;
    private String username;
    private String email;
    private int level;
    private int total_points;
    private List<Long> attendanceIDs;
}