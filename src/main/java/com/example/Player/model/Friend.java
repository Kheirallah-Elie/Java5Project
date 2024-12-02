package com.example.Player.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "friend")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    // Many friends are associated with one player
    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private Long friendID;

    public Friend(Player player, Long friendID) {
        this.player = player;
        this.friendID = friendID;
    }

    // Default constructor (required by JPA)
    public Friend() {}
}
