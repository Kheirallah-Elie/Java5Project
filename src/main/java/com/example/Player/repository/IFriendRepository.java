package com.example.Player.repository;

import com.example.Player.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface IFriendRepository extends JpaRepository<Friend, Long> {
}
