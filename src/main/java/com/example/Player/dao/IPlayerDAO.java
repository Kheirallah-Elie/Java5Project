package com.example.Player.dao;

import com.example.Player.model.Player;
import jakarta.transaction.Transactional;

public interface IPlayerDAO {
    //CRUD Operations using JPA
    @Transactional
    void addPlayer(Player player);

    Player getPlayerById(long id);

    @Transactional
    void updatePlayer(long playerId, Player player);

    @Transactional
    void deletePlayer(long playerId);
}
