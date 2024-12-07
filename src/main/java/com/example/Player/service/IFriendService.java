package com.example.Player.service;

import jakarta.transaction.Transactional;

public interface IFriendService {

    @Transactional
    public void addFriend(long playerId, long friendId);
    @Transactional
    public void deleteFriend(long playerId, long friendId);

}
