package com.example.Player.dao;

import com.example.Player.model.Player;
import com.example.Player.repository.IPlayerRepository;
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

    @Autowired
    private IPlayerRepository playerRepository;

    @Transactional
    public void addPlayer(Player player) {
        playerRepository.save(player);
    }

    @Transactional
    public void updatePlayer(long playerId, Player player) {
        player.setId(playerId);
        playerRepository.save(player);
    }

    @Transactional
    public void deletePlayer(long playerId) {
        playerRepository.deleteById(playerId);
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
