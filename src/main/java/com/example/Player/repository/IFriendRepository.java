package com.example.Player.repository;

import com.example.Player.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface IFriendRepository extends JpaRepository<Friend, Long> {

    @Query("SELECT p.id, p.name, f.id, f.friendID, fp.name " +
            "FROM Player p " +
            "LEFT JOIN p.friends f " +
            "LEFT JOIN Player fp ON f.friendID = fp.id " +
            "WHERE p.id = :playerId " +
            "ORDER BY p.id")
    List<Object[]> getRawFriendsByPlayerId(@Param("playerId") long playerId);

    @Query("SELECT p.id, p.name, f.id, f.friendID, fp.name " +
            "FROM Player p " +
            "LEFT JOIN p.friends f " +
            "LEFT JOIN Player fp ON f.friendID = fp.id " +
            "ORDER BY p.id")
    List<Object[]> getRawAllPlayersWithAllTheirFriends();

    // Sending RAW data so that we can map them in the DAO
}