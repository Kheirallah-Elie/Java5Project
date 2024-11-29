package com.example.Player.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "friend")
public class Friend {
    @Id
    private long id; // Primary key
    private int playerID; // Foreign key
    private int friendID; // Foreign key
}
