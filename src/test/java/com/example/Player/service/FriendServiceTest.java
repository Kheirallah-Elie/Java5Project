package com.example.Player.service;

import com.example.Player.dao.impl.FriendDAO;
import com.example.Player.dao.impl.PlayerDAO;
import com.example.Player.dto.FriendDTO;
import com.example.Player.dto.PlayerWithFriendsDTO;
import com.example.Player.model.Player;
import com.example.Player.service.impl.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FriendServiceTest {

    @InjectMocks
    private FriendService friendService;

    @Mock
    private PlayerDAO playerDAO;

    @Mock
    private FriendDAO friendDAO;

    private Player player;
    private Player friend;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        player = new Player();
        player.setId(1L);
        player.setName("Player1");

        friend = new Player();
        friend.setId(2L);
        friend.setName("Friend1");
    }

    @Test
    void testAddFriend_Success() {
        // Arrange
        when(playerDAO.getPlayerById(1L)).thenReturn(player);
        when(playerDAO.getPlayerById(2L)).thenReturn(friend);

        // Act
        friendService.addFriend(1L, 2L);

        // Assert
        verify(playerDAO, times(1)).addPlayer(player);  // Verify player is saved after adding a friend
        assertTrue(player.getFriends().stream().anyMatch(f -> f.getFriendID().equals(2L)));
        assertTrue(friend.getFriends().stream().anyMatch(f -> f.getFriendID().equals(1L)));
    }

    @Test
    void testAddFriend_PlayerNotFound() {
        // Arrange
        when(playerDAO.getPlayerById(1L)).thenReturn(null);
        when(playerDAO.getPlayerById(2L)).thenReturn(friend);

        // Act
        friendService.addFriend(1L, 2L);

        // Assert
        verify(playerDAO, times(0)).addPlayer(any());  // No player should be saved since player is null
    }

    @Test
    void testDeleteFriend_Success() {
        // Arrange
        when(playerDAO.getPlayerById(1L)).thenReturn(player);
        when(playerDAO.getPlayerById(2L)).thenReturn(friend);

        // Act
        friendService.deleteFriend(1L, 2L);

        // Assert
        verify(playerDAO, times(1)).savePlayer(player);  // Verify player is saved after removing friend
        verify(playerDAO, times(1)).savePlayer(friend);  // Verify friend is saved after removing friend
        assertFalse(player.getFriends().stream().anyMatch(f -> f.getFriendID().equals(2L)));
        assertFalse(friend.getFriends().stream().anyMatch(f -> f.getFriendID().equals(1L)));
    }

    @Test
    void testDeleteFriend_PlayerNotFound() {
        // Arrange
        when(playerDAO.getPlayerById(1L)).thenReturn(null);
        when(playerDAO.getPlayerById(2L)).thenReturn(friend);

        // Act
        friendService.deleteFriend(1L, 2L);

        // Assert
        verify(playerDAO, times(0)).savePlayer(any());  // No player should be saved since player is null
    }

    @Test
    void testGetFriendsByPlayerId() {
        // Arrange
        // Assuming the DAO method returns a list of players with their friends
        PlayerWithFriendsDTO playerWithFriendsDTO = new PlayerWithFriendsDTO(1L, "Player1");
        playerWithFriendsDTO.getFriends().add(new FriendDTO(2L, "Friend1"));
        when(friendDAO.getFriendsByPlayerId(1L)).thenReturn(Arrays.asList(playerWithFriendsDTO));

        // Act
        List<PlayerWithFriendsDTO> result = friendService.getFriendsByPlayerId(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getFriends().get(0).getFriendId());
        assertEquals("Friend1", result.get(0).getFriends().get(0).getFriendName());
    }

    @Test
    void testGetAllPlayersWithFriends() {
        // Arrange
        PlayerWithFriendsDTO playerWithFriendsDTO = new PlayerWithFriendsDTO(1L, "Player1");
        playerWithFriendsDTO.getFriends().add(new FriendDTO(2L, "Friend1"));
        when(friendDAO.getAllPlayersWithFriends()).thenReturn(Arrays.asList(playerWithFriendsDTO));

        // Act
        List<PlayerWithFriendsDTO> result = friendService.getAllPlayersWithFriends();

        // Assert
        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getFriends().get(0).getFriendId());
        assertEquals("Friend1", result.get(0).getFriends().get(0).getFriendName());
    }
}
