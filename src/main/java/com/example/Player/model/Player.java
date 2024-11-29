package com.example.Player.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "player")
public class Player {
    @Id
    private long id;
    private String name;
    private String username;
    private String email;
    private int level;
    private int totalPoints;
    private List<String> friends;
}
