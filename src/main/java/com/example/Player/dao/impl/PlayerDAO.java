package com.example.Player.dao.impl;

import com.example.Player.dao.IPlayerDAO;
import com.example.Player.model.Player;
import com.example.Player.repository.IPlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;

@Repository
public class PlayerDAO implements IPlayerDAO {

    @Autowired
    private IPlayerRepository playerRepository;

    //CRUD Operations using JPA
    @Override
    @Transactional
    public void addPlayer(Player player) {
        playerRepository.save(player);
    }
    @Override
    public Player getPlayerById(long id) {
        return playerRepository.getReferenceById(id);
    }
    @Override
    @Transactional
    public void updatePlayer(long playerId, Player player) {
        player.setId(playerId);
        playerRepository.save(player);
    }
    @Override
    @Transactional
    public void deletePlayer(long playerId) {
        playerRepository.deleteById(playerId);
    }



    //  Useful method to find and add friends with ease // Not implemented in the interface
    @Transactional
    public void savePlayer(Player player) {
        playerRepository.save(player);
    }

}
