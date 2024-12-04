package com.example.Player.dao;

import com.example.Player.model.Player;
import com.example.Player.repository.IPlayerRepository;
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

    //CRUD Operations using JPA
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




    // Methods useful to find and add friends
    public Player findPlayerById(long id) {
        return entityManager.find(Player.class, id);
    }

    public void savePlayer(Player player) {
        entityManager.persist(player);
    }

}
