package com.example.Player.service;

import com.example.Player.dto.PlayerAddDTO;
import com.example.Player.dto.PlayerDTO;
import com.example.Player.model.Player;
import jakarta.transaction.Transactional;


public interface IPlayerService {
    @Transactional
    void addPlayer(PlayerAddDTO playerAddDTO);
    Player getPlayerById(long playerId);
    @Transactional
    void updatePlayer(long playerId, PlayerDTO playerDTO);
    @Transactional
    void deletePlayer(long playerId);

}
