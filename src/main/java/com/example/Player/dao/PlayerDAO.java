package com.example.Player.dao;

import com.example.Player.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import java.util.List;

@Repository
public class PlayerDAO {

    @Autowired
    private EntityManager entityManager;

    public List<Player> findAllPlayers() {
        String query = "SELECT p FROM Player p"; // Here we can inject any SQL request we want
        return entityManager.createQuery(query, Player.class).getResultList();
    }

    public Player findPlayerById(long id) {
        return entityManager.find(Player.class, id);
    }

    public Player savePlayer(Player player) {
        entityManager.persist(player);
        return player;
    }

    public Player updatePlayer(Player player) {
        return entityManager.merge(player);
    }

    public void deletePlayer(long id) {
        Player player = findPlayerById(id);
        if (player != null) {
            entityManager.remove(player);
        }
    }
}
