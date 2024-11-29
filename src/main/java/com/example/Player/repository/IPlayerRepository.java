package com.example.Player.repository;

import com.example.Player.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface IPlayerRepository extends JpaRepository<Player, Long> {
}
