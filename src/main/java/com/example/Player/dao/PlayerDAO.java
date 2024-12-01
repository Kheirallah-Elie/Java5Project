package com.example.Player.dao;

import com.example.Player.model.Player;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

import java.util.List;

@Repository
public class PlayerDAO {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void addPlayer(Player player) {
        String sqlQuery = """
        INSERT INTO player (name, username, email, level, total_points)
        VALUES (:name, :username, :email, :level, :total_points)
    """;

        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("name", player.getName());
        query.setParameter("username", player.getUsername());
        query.setParameter("email", player.getEmail());
        query.setParameter("level", player.getLevel());
        query.setParameter("total_points", player.getTotal_points());
        query.executeUpdate();
    }

    @Transactional
    public void updatePlayer(long playerId, Player player) {
        String sqlQuery = """
        UPDATE player
        SET name = :name, username = :username, email = :email, level = :level, total_points = :total_points
        WHERE id = :playerId
    """;

        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("name", player.getName());
        query.setParameter("username", player.getUsername());
        query.setParameter("email", player.getEmail());
        query.setParameter("level", player.getLevel());
        query.setParameter("total_points", player.getTotal_points());
        query.setParameter("playerId", playerId);
        query.executeUpdate();
    }

    @Transactional
    public void deletePlayer(long playerId) {
        String sqlQuery = """
            DELETE FROM player
            WHERE id = :playerId
        """;

        Query query = entityManager.createNativeQuery(sqlQuery);
        query.setParameter("playerId", playerId);
        query.executeUpdate();
    }


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

}
