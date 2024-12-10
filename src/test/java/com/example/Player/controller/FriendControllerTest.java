package com.example.Player.controller;

import com.example.Player.dto.FriendDTO;
import com.example.Player.dto.PlayerWithFriendsDTO;
import com.example.Player.service.impl.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FriendControllerTest {

    @InjectMocks
    private FriendController friendController;

    @Mock
    private FriendService friendService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(friendController).build();
    }

    @Test
    void addFriend_ShouldReturnSuccessMessage() throws Exception {
        long playerId = 1L;
        long friendId = 2L;

        doNothing().when(friendService).addFriend(playerId, friendId);

        mockMvc.perform(post("/friends/" + playerId + "/addFriend/" + friendId))
                .andExpect(status().isOk())
                .andExpect(content().string("Friend added."));

        verify(friendService, times(1)).addFriend(playerId, friendId);
    }

    @Test
    void deleteFriend_ShouldReturnSuccessMessage() throws Exception {
        long playerId = 1L;
        long friendId = 2L;

        doNothing().when(friendService).deleteFriend(playerId, friendId);

        mockMvc.perform(delete("/friends/" + playerId + "/delete/" + friendId))
                .andExpect(status().isOk())
                .andExpect(content().string("Friend deleted."));

        verify(friendService, times(1)).deleteFriend(playerId, friendId);
    }

    @Test
    void getAllPlayersWithFriends_ShouldReturnListOfPlayersWithFriends() throws Exception {
        PlayerWithFriendsDTO playerWithFriends = new PlayerWithFriendsDTO(1L, "Player1");
        playerWithFriends.getFriends().add(new FriendDTO(2L, "Friend1"));


        List<PlayerWithFriendsDTO> playersWithFriends = Arrays.asList(playerWithFriends);
        when(friendService.getAllPlayersWithFriends()).thenReturn(playersWithFriends);

        mockMvc.perform(get("/friends/playersWithFriends"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].playerId").value(1L))
                .andExpect(jsonPath("$[0].playerName").value("Player1"))
                .andExpect(jsonPath("$[0].friends[0].friendId").value(2L))
                .andExpect(jsonPath("$[0].friends[0].friendName").value("Friend1"));

        verify(friendService, times(1)).getAllPlayersWithFriends();
    }

    @Test
    void getFriendsByPlayerId_ShouldReturnListOfFriends() throws Exception {
        PlayerWithFriendsDTO playerWithFriends = new PlayerWithFriendsDTO(1L, "Player1");
        playerWithFriends.getFriends().add(new FriendDTO(2L, "Friend1"));


        List<PlayerWithFriendsDTO> friendsByPlayerId = Arrays.asList(playerWithFriends);
        when(friendService.getFriendsByPlayerId(1L)).thenReturn(friendsByPlayerId);

        mockMvc.perform(get("/friends/player/1/friends"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].playerId").value(1L))
                .andExpect(jsonPath("$[0].playerName").value("Player1"))
                .andExpect(jsonPath("$[0].friends[0].friendId").value(2L))
                .andExpect(jsonPath("$[0].friends[0].friendName").value("Friend1"));

        verify(friendService, times(1)).getFriendsByPlayerId(1L);
    }
}