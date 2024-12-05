package com.example.Player.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String username;
    private String email;
    private int level;
    private int total_points;
    private List<Long> attendanceIDs; // here I am creating a list of all the attendance IDs that the player will have

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> friends;

    // method to add a friend
    public void addFriend(Player friendPlayer) {
        Friend friend = new Friend(this, friendPlayer.getId());
        friends.add(friend);
        friendPlayer.getFriends().add(new Friend(friendPlayer, this.getId())); // Add reverse
    }

    // Method to remove a friend
    public void removeFriend(Long friendId) {
        friends.removeIf(friend -> Objects.equals(friend.getFriendID(), friendId));
    }
}
