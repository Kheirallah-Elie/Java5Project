# Project Documentation: Player and Game Management System
## Author: Elie Kheirallah
### Academic Year: 2024-2025

---

## Overview
This project involves developing a multi-player online game management application using **Spring Boot**. The system is divided into two microservices:

1. **Player Management Service**:
   - Handles player profiles, points, and their friends.
2. **Game Management Service**:
   - Manages game sessions/attendances, scores, and player performance.

The microservices communicate via REST APIs to synchronize player statistics with game outcomes.

---

# Table of Contents

1. [Architecture](#architecture)
   - [Controller Layer](#controller-layer)
   - [Service Layer](#service-layer)
   - [DAO Layer](#dao-layer)
   - [Repository Layer](#repository-layer)
   - [Entity Layer](#entity-layer)
   - [DTO Layer](#dto-layer)
   
2. [Player Project](#player-project)
   - [Entities and Models](#entities-and-models)
     - [Player Entity](#player-entity)
       - [Attributes](#attributes)
       - [Relationships](#relationships)
     - [Friend Entity](#friend-entity)
       - [Attributes](#attributes-1)
       - [Relationships](#relationships-1)
   - [Data Transfer Objects (DTOs)](#data-transfer-objects-dtos)
     - [PlayerAddDTO](#playeradddto)
     - [PlayerDTO](#playerdto)
     - [PlayerWithFriendsDTO](#playerwithfriendsdto)
   - [Controller](#controller)
     - [Example of Creating a New Player](#example-of-creating-a-new-player---step-by-step)
   - [Repositories](#repositories)
     - [Player Repository](#player-repository)
     - [Friend Repository](#friend-repository)
   - [DAO Layer](#dao-layer-1)
     - [PlayerDAO](#playerdao)

3. [Game Project](#game-project)
   - [Entities and Models](#entities-and-models-1)
     - [Game Entity](#game-entity)
       - [Attributes](#attributes-2)
       - [Relationships](#relationships-2)
     - [Attendance Entity](#attendance-entity)
       - [Attributes](#attributes-3)
       - [Relationships](#relationships-3)
   - [RestAPI Communication](#restapi-communication-with-the-previous-player-project)
     - [Updating Player Points](#updating-player-points)

4. [Endpoints](#endpoints)
   - [PlayerController Endpoints](#playercontroller-endpoints)
   - [FriendController Endpoints](#friendcontroller-endpoints)
   - [GameController Endpoints](#gamecontroller-endpoints)
   - [AttendanceController Endpoints](#attendancecontroller-endpoints)

5. [Database](#database)
   - [Database Schema](#database-schema)
     - [Player Management Service](#1-player-management-service)
       - [Player Table](#player)
       - [Friend Table](#friend)
     - [Game Management Service](#2-game-management-service)
       - [Game Table](#game)
       - [Attendance Table](#attendance)
   - [Communication Between Services](#communication-between-services)

6. [PostGreSQL Configuration](#postgresql)
7. [Manually creating the tables with SQL Queries](#manually-creating-the-tables-with-sql-queries)
8. [Testing](#testing)
8. [Conclusion](#conclusion)

---

## Architecture

This application follows a **Layered Architecture**:
1. **Controller Layer**: To handle HTTP requests and responses.
2. **Service Layer**: To implement business logic.
3. **DAO Layer**: To manage interaction with the PostGreSQL database.
4. **Repository Layer**: To provide database access using JPA and JPQL custom queries.
5. **Entity Layer**: To define the database schema using JPA relations.
6. **DTO Layer**: To define what data can be transfered or modified.

---

# Player project

---

## Entities and Models

Let's start by describing the entities and their attributes. This will be the 1st project **Player** that contains both Player and Friend models. Later on we will discuss how the 2nd project interract (using **RestAPI**) with this one.
### Player Entity

``` java
@Data
@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String username;
    private String email;
    private int level;
    private int total_points;
    private List<Long> attendanceIDs; // here I am creating a list of all the attendance IDs that the player will receive through RestAPI calls.

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> friends;

    // method to add a friend
    public void addFriend(Player friendPlayer) {
        Friend friend = new Friend(this, friendPlayer.getId());
        friends.add(friend);
        friendPlayer.getFriends().add(new Friend(friendPlayer, this.getId())); // Add reverse
    }

    // Method to remove a friend
    public void removeFriend(Long friendId) {
        friends.removeIf(friend -> Objects.equals(friend.getFriendID(), friendId));
    }
}
```


## Explanation

### Attributes:
- **id**: Unique identifier for each player.
- **name, username, email**: Player profile information.
- **level**: Player's level in the game.
- **total_points**: Total score accumulated updated through RestAPI from the Game project.

### Relationships:
- **Player-Friend Relationship, JPA relationship**:  
  A Player can have multiple friends (*OneToMany* with `Friend`).
- **attendanceIDs - Rest API relationship**: Tracks a list of IDs of games the player has participated in. This will be communicating with the Game project through RestAPI since the Attendance entity is not in this project.


### Friend Entity

``` JAVA
@Data
@Entity
@Table(name = "friend")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    // Many friends are associated with one player
    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private Long friendID;

    public Friend(Player player, Long friendID) {
        this.player = player;
        this.friendID = friendID;
    }

    // Default constructor (required by JPA)
    public Friend() {}
}
```

## Explanation
### Attributes:
- **id**: Primary key for each friend record, generated automatically with **@GeneratedValue**.
- **player**: The player who owns this friendship.
- **friendID**: The ID of the associated friend.

### Relationships:
- **Friend-Player Relationship**: 
A friend is owned by one player.


## Data Transfer Objects (DTOs)

Now we will create some DTOs to access or manage a limited amount of data.

### PlayerAddDTO
```java 
@Data
public class PlayerAddDTO {
    private String name;
    private String username;
    private String email;
}
```

- **Purpose**:
Used for creating new players without creating level or total_points, as these will be updated later when games are created and played.

QUICK NOTE: **@Data** is a **Lombok** annotation that automatically provide **Getters** and **Setters** for all the object's attributes.


### PlayerDTO
``` JAVA
@Data
public class PlayerDTO {
    private long id;
    private String name;
    private String username;
    private String email;
    private int level;
    private int total_points;
    private List<Long> attendanceIDs;
}
```
- **Purpose**:
A more complete DTO used for updating and retrieving player information, including points and attendance records.

### PlayerWithFriendsDTO
``` JAVA
@Data
public class PlayerWithFriendsDTO {
    private long playerId;
    private String playerName;
    private List<FriendDTO> friends = new ArrayList<>();

    public PlayerWithFriendsDTO(long playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
    }
}
```
- **Purpose**:
Represents a player with their list of friends. This is optional, and will return a JSON as follows using a special Mapping in the FriendDAO class:
```JSON

    {
        "playerId": 1,
        "playerName": "John Doe",
        "friends": [
            {
                "friendId": 1,
                "friendName": "Jane Smith"
            },
            {
                "friendId": 2,
                "friendName": "Alice Johnson"
            }
        ]
    },
    {
        "playerId": 2,
        "playerName": "Jane Smith",
        "friends": [
            {
                "friendId": 3,
                "friendName": "John Doe"
            }
        ]
    }
```

For example, this shows that John Doe has Jane Smith and Alice Johnson as friends. And Jane Smith has John Doe as friend. PS: Friends are added or deleted both ways automatically in the FriendService class:

``` Java
    public void addFriend(long playerId, long friendId) {
        Player player = playerDAO.findPlayerById(playerId);  // Fetch the player entity
        Player friend = playerDAO.findPlayerById(friendId);  // Fetch the friend entity

        if (player != null && friend != null) {
            player.addFriend(friend);  // Add friend in both directions
            playerDAO.addPlayer(player);    // Save the player entity, JPA will cascade
        }
    }

    public void deleteFriend(long playerId, long friendId) {
        Player player = playerDAO.findPlayerById(playerId);
        Player friend = playerDAO.findPlayerById(friendId);

        if (player != null && friend != null) {
            player.removeFriend(friendId);  // Remove the friend from player
            friend.removeFriend(playerId);  // Remove the reverse relationship
            playerDAO.savePlayer(player);   // Save the updated player
            playerDAO.savePlayer(friend);   // Save the updated friend
        }
    }
```


Now let's dive into how this will interract with the database, focusing mainly on the Player's side.

## Controller

### Example of Creating a new Player - Step by step

To create a new player, since we're using Localhost 8081 for this project, we need to call this URL
http://localhost:8081/players/add/ 
to call this method:

```JAVA
@RequestMapping("/players")
public class PlayerController {
@PostMapping("/add")
public String addPlayer(@RequestBody PlayerAddDTO playerAddDTO) {
    playerService.addPlayer(playerAddDTO);
    return "Player added successfully!";
}
```

We need to add data in a JSON structure as follows and select "Post" in Postman

 ```JSON
"name": "Jane Smith",
"username": "janesmith",
"email": "jane@example.com",
 ```

 **Note that we are using PlayerAddDTO to add a player, and the other attributes such as "level" and "total_points" are restricted here, and the ID is automatically generated.**

What happens next: The controller will call the PlayerService class ```playerService.addPlayer(playerAddDTO);```

Then this method in the PlayerService will create a new player to add their Name, Username and Email, using the PlayerAddDTO to later send the Player object to the PlayerDAO:

``` JAVA
    @Override
    public void addPlayer(PlayerAddDTO playerAddDTO) { // I decided to add players and restrict adding total points and level at first through the PlayerAddDTO
        Player player = new Player();
        player.setName(playerAddDTO.getName());
        player.setUsername(playerAddDTO.getUsername());
        player.setEmail(playerAddDTO.getEmail());
        playerDAO.addPlayer(player);
    }
```

Then the PlayerDAO will ask the JPA IPlayerRepository to add this new player to the database.

``` JAVA
    public void addPlayer(Player player) {
        playerRepository.save(player);
    }
```



The new player is now added to the database!

Let's quickly describe how the Repositories work next.


## Repositories
### Player Repository
```JAVA
@RepositoryRestResource
public interface IPlayerRepository extends JpaRepository<Player, Long> {}
```
Player repository will extend the JpaRepository to be able to use standard JPA's **CRUD** operations like the **CREATE** we saw above.


### Friend Repository

Now for the Friend Repository, I have decided to create some custom JPQL queries that can be used on top of the basic CRUD operations.

``` JAVA
@RepositoryRestResource
public interface IFriendRepository extends JpaRepository<Friend, Long> {
    @Query("SELECT p.id, p.name, f.id, f.friendID, fp.name " +
           "FROM Player p " +
           "LEFT JOIN p.friends f " +
           "LEFT JOIN Player fp ON f.friendID = fp.id " +
           "WHERE p.id = :playerId " +
           "ORDER BY p.id")
    List<Object[]> findRawFriendsByPlayerId(@Param("playerId") long playerId);

    @Query("SELECT p.id, p.name, f.id, f.friendID, fp.name " +
           "FROM Player p " +
           "LEFT JOIN p.friends f " +
           "LEFT JOIN Player fp ON f.friendID = fp.id " +
           "ORDER BY p.id")
    List<Object[]> findRawAllPlayersWithAllTheirFriends();
}
```

These will select a list of friends for a specific player based on their playerId and to fetch all players along with their associated friends, returning a RAW table to be later correctly mapped into the JSON structure shown earlier within the FriendDAO. 


## DAO Layer
### PlayerDAO
``` JAVA
@Repository
public class PlayerDAO implements IPlayerDAO {
    @Autowired
    private IPlayerRepository playerRepository;

    @Override
    public void addPlayer(Player player) {
        playerRepository.save(player);
    }
    @Override
    public Player getPlayerById(long id) {
        return playerRepository.getReferenceById(id);
    }
    @Override
    public void updatePlayer(long playerId, Player player) {
        player.setId(playerId);
        playerRepository.save(player);
    }
    @Override
    public void deletePlayer(long playerId) {
        playerRepository.deleteById(playerId);
    }
}
```

Here you have the methods to add, get, update and delete players in the database using the JPA Repository services, that does simple CRUD operations.

---
# Game project
---
## Entities and Models

Let's start by describing the entities and their attributes. This is the 2nd project that contains **Game** and **Attendance** models. 

### Game Entity

``` java
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "date_played")
    private Date datePlayed;

    private String type;

    @Column(name = "max_score")
    private int maxScore;

    @Column(name = "id_host")
    private int idHost; // Foreign Key

    // One game can have many attendances
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendances;
}
```


## Explanation

### Attributes:
- **id**: Unique identifier for each game.
- **name, datePlayer, type, maxScore**: Game information.
- **idHost**: The ID of the Player who will be creating this game.

### Relationships:
- **Game-Attendance Relationship, JPA relationship**:  
  A Game can have multiple Attendances (*OneToMany* with `Attendance`).

**Note that the camelCase convention may cause some issues with the snake_case of PostGreSQL, hence why annotating the Column name seems to be a better practice to avoid any confusion with the table names.**

### Attendance Entity

``` JAVA
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    // Linking to the Player ID fetched via REST API
    @Column(name = "player_id")
    private Long playerId; // ID of the player (linked via REST API)

    private int score;
    private boolean isWinner;
}
```

## Explanation
### Attributes:
- **id**: Primary key for each Attendance record, generated automatically with **@GeneratedValue**.
- **score**: The score of the attendance that will be added to the Player's total_points attribute through a RestAPI call.
- **isWinner**: A boolean value to tell if the attendee is the winner of that attendance or not.

### Relationships:
- **Attendance-Game Relationship**: 
An attendance is owned by one Game.

---
## RestAPI communication with the previous Player project

### Updating Player points
Here is an example of a RestAPI call to update a player's total point.

http://localhost:8083/attendances/UpdatePlayerPoints/12/550 

We are using Port 8083 on the Game project, and calling the Attendance controller's **UpdatePlayer** function, followed by the ID of the Player we wish to update, and the number of score we wish to increment. 

```java
    @PutMapping("/UpdatePlayerPoints/{id}/{score}")
    public String updatePlayer(@PathVariable long id, @PathVariable int score) {
        attendanceService.updatePlayerPoints(id, score);
        return "Player updated successfully!";
    }
```
This will then call the AttendnaceService class that will handle the RestAPI call in this method

``` Java
    @Override
    public void updatePlayerPoints(long playerId, int score) {
        String url = "http://localhost:8081/players/" + playerId + "/updatePoints/" + score;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(url, null, Void.class);  // Call Player API to update points
    }
```

This method is then calling the first project on Port 8081, where it will use one of the player's controller to update their existing points. 

So now our code left to trigger this function in our PlayerService's class to update their points

```java
    @Transactional
    public void updatePlayerPoints(long playerId, int points) {
        Player player = getPlayerById(playerId);
        if (player != null) {
            player.setTotal_points(player.getTotal_points() + points);  // Add points to total
            playerDAO.updatePlayer(playerId, player);
        } else {
            throw new RuntimeException("Player not found with id: " + playerId);
        }
    }
```

In this function **in the first Player project** PlayerService class, the player will receive the ID of the Player that needs to be updated, and will increment its existing points to the points received by the Attendance class from the second project. 

Then PlayerDAO will **update** the new Player using one of the previously described **CRUD** operation!

Other methods in the **AttendanceService** class are used to communicate with the first project through RestAPI call such as these ones

``` java
    public PlayerDTO getPlayerDetails(long playerId) {
        String url = "http://localhost:8081/players/" + playerId;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, PlayerDTO.class);  // Call Player API to get player details
    }

    private void updatePlayerAttendance(long playerId, long attendanceId) {
        String url = "http://localhost:8081/players/" + playerId + "/addAttendance/" + attendanceId;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(url, null, Void.class);  // Call Player API to update attendance list
    }
```

The first one required re-creating a **PlayerDTO** class in the second project to fetch all the player's details from the first project using the Player's ID. 
This will be useful to make sure a Player exists before adding an attendance 

The last method will be updating the player's list of attendances after verifying that the player exists within the database in this following method

```Java
    @Override
    public Attendance createAttendance(Long gameId, Long playerId) {
        // Fetch game details from the database
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        // Fetch player details by making a REST API call to server 8081
        PlayerDTO playerDTO = getPlayerDetails(playerId);

        // Ensure that the playerDTO is not null
        if (playerDTO == null) {
            throw new IllegalArgumentException("Player not found");
        }

        // Create and save the attendance record
        Attendance attendance = new Attendance();
        attendance.setGame(game);  // Set game
        attendance.setPlayerId(playerDTO.getId());  // Assuming `PlayerDTO` contains the playerId field

        updatePlayerAttendance(playerId, gameId);
        // Now save the Attendance to the database
        return attendanceDAO.save(attendance);
    }
```
In this method, before creating an attendance, we are first verifying if the Game we're trying to attend exists, then we are fetching the Player who will attend that Game through the **RestAPI** method and adding those details in the locally created **PlayerDTO** data transfer object class. 

Then we verify if that **DTO** is **not null** to make sure the player was fetched correctly or exist in the database. 

If all is well, a new **Attendance** with its **player's ID** is created.

Then through the last **RestAPI** call, the **Player** who is **attending** that game is being updated with a ***new attendance***. 

The rest of the flow is quite similar to the first project, using *DAOs* and *JPA* repository to handle **CRUD** operations.

---
# Endpoints
---
Now that we have covered most of the functionalities of this application, it's time to show all the **Endpoints** that this Backend offers and what they accomplish. 

## PlayerController Endpoints

``` java
@RequestMapping("/players")
public class PlayerController {
    @PostMapping("/add")
    @PutMapping("/update/{id}")
    @DeleteMapping("/delete/{id}")
    @GetMapping("/all")
    @GetMapping("/{id}")
    @PostMapping("/{id}/updatePoints/{points}")
    @PostMapping("{id}/addAttendance/{attendanceId}")
}
```

**/player/add** : This is a **Create** operation that will add a new player using the simpler PlayerAddDTO in this JSON format passed through the body: 
```json
{
  "name": "Elie",
  "username": "Eliekhopter",
  "email": "Elie@cvdexample.com"
}
```
**/players/update/{id}** : This is an **Update** operation that will update the selected player (with its ID passed in the URL) using the more complete PlayerDTO with this JSON format instead:
```json
{
  "name": "ElieIsNowUpdated",
  "username": "Eliekhopter",
  "email": "Elie@cvdexample.com",
  "level": 1500,
  "totalPoints": 100
}
```
**/players/delete/{id}** : This is a **Delete** operation that will simply deleted the selected player (with its ID passed in the URL). No JSON body required in this operation.


**/players/all** : This is a **Read* operation that will read all the players that are in the database using a re-arranged list of PlayerDTO.

**/players/{id}** : This is a **Read* operation that will read only the selected player through their ID in the URL, also using a re-arranged list of PlayerDTO.

**/players/{id}/updatePoints/{points}** : This will be used by the **Game** project, as a RestAPI endpoint, to update the points of a player, based on the ID of the player and the points to add, passed through the URL.

**/players/{id}/addAttendance/{attendanceId}** : This will also be used by the **Game** project, as a RestAPI endpoint, to add a new attendance of a Game to a selected player.


## FriendController Endpoints

``` java

@RequestMapping("/friends")
public class FriendController {
    @PostMapping("/{playerId}/addFriend/{friendId}")
    @DeleteMapping("/{playerId}/delete/{friendId}")
    @GetMapping("/playersWithFriends")
    @GetMapping("/player/{id}/friends")
}
```

**/friends/{playerId}/addFriend/{friendId}** : This is a **Create** operation that will select the player ID that will receive a new player ID as a friend. Note that this works both ways, if ID1 adds ID2, ID2 will also add ID1 to their list of friends.

**/friends/{playerId}/delete/{friendId}** : This is a **Delete** operation that will select the player ID that will delete an existing player ID from their friends. Note that this works both ways, if ID1 deletes ID2, ID2 will also delete ID1 from their list of friends.

**/friends/playersWithFriends** : This is a **Read** operation that is using a special mapping based on the **PlayersWithFriendsDTO** to showcase a list of all the existing users with each one the list of their friends as follows 

```json
[
    {
        "playerId": 1,
        "playerName": "John Doeerere",
        "friends": [
            {
                "friendId": 1,
                "friendName": "Jane Smith"
            },
            {
                "friendId": 2,
                "friendName": "Alice Johnson"
            }
        ]
    },
    {
        "playerId": 2,
        "playerName": "Jane Smith",
        "friends": [
            {
                "friendId": 3,
                "friendName": "John Doeerere"
            }
        ]
    },
    {
        "playerId": 3,
        "playerName": "Alice Johnson",
        "friends": [
            {
                "friendId": 5,
                "friendName": "John Doeerere"
            }
        ]
    }
]
```

**/friends/player/{id}/friends** : This is the same as above but only by selecting one player based on their ID.

## GameController Endpoints

``` java

@RequestMapping("/games")
public class GameController {
    @PostMapping("/create")
    @PutMapping("/update/{id}")
    @DeleteMapping("/delete/{id}")
    @GetMapping("/all")
}
```
**/games/create** : This is a **Create** operation that will create a new game using this JSON in the body, 
```JSON
    {
        "name": "Chess",
        "type": "double",
        "maxScore": 1000,
        "idHost": 2
    }
```
**/games/update/{id}** : This is an **Update** operation to update the game details using the **same** **JSON** structure in the body as with the create operation above.

**/games/delete/{id}** : This is a **Delete** operation to delete a game through a selected **ID** in the **URL**, no JSON body required.

**/games/all** : This is a **Read** operation using the GameDTO to map all the existing games in a JSON format as follows

``` json
[
    {
        "name": "Multiplayer Quiz",
        "datePlayed": "2024-12-07T21:42:17.770+00:00",
        "type": "multiplayer",
        "maxScore": 1500,
        "idHost": 1
    },
    {
        "name": "Need For Speed",
        "datePlayed": "2024-12-08T00:11:23.328+00:00",
        "type": "single",
        "maxScore": 1500,
        "idHost": 3
    },
    {
        "name": "Chess",
        "datePlayed": "2024-12-08T00:11:40.661+00:00",
        "type": "double",
        "maxScore": 1000,
        "idHost": 2
    }
]
```

## AttendanceController Endpoints

``` java

@RequestMapping("/attendances")
public class AttendanceController {
    @GetMapping("/{id}")
    @PostMapping("/create/{playerId}/attending/{gameId}")
    @PutMapping("/update/{id}")
    @DeleteMapping("/delete/{id}")
    @PutMapping("/UpdatePlayerPoints/{id}/{score}")
}
```
**/attendances/{id}** : Gets the attendance through the attendance ID and maps it to AttendanceDTO.

**/attendances/create/{playerId}/attending/{gameId}** : This will **Create** a new attendance and mark the player ID who is responsible of that attendance. This will fetch from the player project if the Player **exists** through a RestAPI call, and if they do, the attendance will be added to the database, and also added to the list of attendance to the Player's project through a second RestAPI call.

**/attendances/update/{id}** : This will **Update** the score or the status (is Winner or not) of the attendance.

**/attendances/delete/{id}** : This will **Delete** the attendance, although it may be unecessary. 

**/attendances/UpdatePlayerPoints/{id}/{score}** : This will update the points of a player assuming once the attendance is finished, this endpoint could be used to increment the player's point using a RestAPI call again.


---
# Database

## Database Schema

### 1. Player Management Service

#### Tables:

##### `Player`
| Column         | Type        | Constraints            |
|----------------|-------------|------------------------|
| `id`           | BIGINT      | PRIMARY KEY, AUTO_INCREMENT |
| `name`         | VARCHAR(255)| NOT NULL              |
| `username`     | VARCHAR(255)| UNIQUE, NOT NULL      |
| `email`        | VARCHAR(255)| UNIQUE, NOT NULL      |
| `level`        | INT         | NOT NULL, DEFAULT 1   |
| `total_points` | INT         | NOT NULL, DEFAULT 0   |

##### `Friend`
| Column         | Type        | Constraints            |
|----------------|-------------|------------------------|
| `id`           | BIGINT      | PRIMARY KEY, AUTO_INCREMENT |
| `player_id`    | BIGINT      | FOREIGN KEY REFERENCES `Player(id)` ON DELETE CASCADE |
| `friend_id`    | BIGINT      | FOREIGN KEY REFERENCES `Player(id)` ON DELETE CASCADE |

#### Relationships:
- A **Player** can have many **Friends** (One-to-Many via `Friend` table).

---

### 2. Game Management Service

#### Tables:

##### `Game`
| Column         | Type        | Constraints            |
|----------------|-------------|------------------------|
| `id`           | BIGINT      | PRIMARY KEY, AUTO_INCREMENT |
| `date`         | DATE        | NOT NULL              |
| `game_type`    | VARCHAR(100)| NOT NULL              |
| `max_score`    | INT         | NOT NULL              |
| `host_id`      | BIGINT      | FOREIGN KEY REFERENCES `PlayerManagementService.Player(id)` |

##### `Attendance`
| Column         | Type        | Constraints            |
|----------------|-------------|------------------------|
| `id`           | BIGINT      | PRIMARY KEY, AUTO_INCREMENT |
| `game_id`      | BIGINT      | FOREIGN KEY REFERENCES `Game(id)` ON DELETE CASCADE |
| `player_id`    | BIGINT      | FOREIGN KEY REFERENCES `PlayerManagementService.Player(id)` ON DELETE CASCADE |
| `score`        | INT         | NOT NULL              |
| `is_winner`      | BOOLEAN     | NOT NULL              |

#### Relationships:
- A **Game** can have multiple **Participants** (Many-to-Many via `Participation` table).
- A **Player** can participate in multiple **Games** but this will be handled through **RestAPI** calls.

---

### Communication Between Services

#### Overview:
- The **Game Management Service** sends a REST API request to the **Player Management Service** after a game ends.
- This updates the **Player**'s `total_points` and `level` based on the performance recorded in the **Participation** table.


---

# PostGreSQL

The database used in this project is **PostGreSQL**, to achieve that, the port and the name of the database need to be specified in the **application.properties** file under the **resources** folder as follows:

``` python
spring.application.name=Game
server.port=8083
spring.datasource.url = jdbc:postgresql://localhost:5432/mydatabase
spring.datasource.username = postgres
spring.datasource.password = Admin4321

spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```


**This line is responsible for creating the database schema and managing the relationships:**

```Java
spring.jpa.hibernate.ddl-auto=update
```

It configures **Hibernate**, the **JPA** implementation used by **Spring Boot**, to manage the database schema **automatically**.

**Hibernate** will check the database schema on application startup.
If tables, columns, or relationships defined in the entity classes (@Entity, @OneToMany, etc.) do not exist, **Hibernate** will automatically add them to the database.
Existing schema elements are **preserved**, <ins>and only necessary updates are applied.</ins>
By defining the tables's relationships in Java classes using annotations like *@Entity, @Table, @OneToMany, and @ManyToOne,* **Hibernate** reads these entity definitions and compares them to the database schema.


If there are differences, **Hibernate** modifies the schema to match the entity definitions.

This simplifies development by automatically syncing your database schema with your entity models.

### Caution:

This setting is great for development but can be **risky** in production. It could **modify** the database schema, potentially causing **data loss**. For production, it's best to use **ddl-auto=validate** or generate the tables manually as follows.

# Manually creating the tables with SQL Queries

```SQL
CREATE TABLE player (
    id BIGSERIAL PRIMARY KEY, -- Primary key
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    level INT NOT NULL,
    total_points INT NOT NULL
);

CREATE TABLE friend (
    id BIGSERIAL PRIMARY KEY, -- Primary key for the table
    player_id BIGINT NOT NULL, -- Foreign key to the player table
    friend_id BIGINT NOT NULL, -- Stores the ID of the friend

    CONSTRAINT fk_player FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE CASCADE
);

CREATE TABLE attendance (
    id BIGSERIAL PRIMARY KEY, -- Primary key
    game_id BIGINT NOT NULL, -- Foreign key to the game table
    player_id BIGINT NOT NULL, -- Player ID (linked to the Player microservice via REST API)
    score INT NOT NULL, -- Score achieved by the player
    is_winner BOOLEAN NOT NULL, -- Indicates if the player is the winner

    CONSTRAINT fk_game FOREIGN KEY (game_id) REFERENCES game (id) ON DELETE CASCADE
);


CREATE TABLE game (
    id BIGSERIAL PRIMARY KEY, -- Primary key
    name VARCHAR(255) NOT NULL,
    date_played DATE, -- Stores the date the game was played
    type VARCHAR(100), -- Game type (e.g., strategy, action)
    max_score INT NOT NULL, -- Maximum score achievable in the game
    id_host INT NOT NULL -- Host ID (links to the host system via REST API)
);
```

**Note**: The player_id is fetched via **REST API** from the **Player* service*, and game_id links the attendance to the respective game.
And The player_id in the attendance table is not directly tied to a foreign key in the **Player** database but is retrieved via **REST API** calls when needed.


The **Friend** constraint ensures that if a *player* is deleted from the **Player** table, all rows in another table (e.g., Friend) that reference that *player*'s ID are also automatically deleted. It helps keep the database clean by removing related data that is no longer relevant.

Similarly when deleting a **Game**, the constraint would automatically delete any **Attendance** associated with it. 


---
# Testing

A few **Mock** tests were running on PlayerController, FriendController, PlayerService, FriendService, AttendanceController, AttendanceService, GameController and GameService.

Let's take an example of how the method in the **AttendanceService** containing the logic to correctly add a update an existing attendance in the **Attendance** table

```java
    @Test
    void testUpdateAttendance() {
        long attendanceId = 1L;
        AttendanceDTO attendanceDTO = new AttendanceDTO();
        attendanceDTO.setScore(15);
        attendanceDTO.setWinner(true);

        Attendance attendance = new Attendance();
        attendance.setScore(10);
        attendance.setWinner(false);

        when(attendanceDAO.findById(attendanceId)).thenReturn(Optional.of(attendance));
        when(attendanceDAO.updateAttendance(any(Attendance.class))).thenReturn(attendance);

        AttendanceDTO result = attendanceService.updateAttendance(attendanceId, attendanceDTO);

        assertNotNull(result);
        assertEquals(15, result.getScore());
        assertTrue(result.isWinner());
        verify(attendanceDAO, times(1)).findById(attendanceId);
        verify(attendanceDAO, times(1)).updateAttendance(any(Attendance.class));
    }
```
In this case, we are testing this method in the **AttendanceService**:

``` java 
    @Override
    public AttendanceDTO updateAttendance(long id, AttendanceDTO attendanceDTO) {
        Optional<Attendance> optionalAttendance = attendanceDAO.findById(id);
        if (optionalAttendance.isPresent()) {
            Attendance attendance = optionalAttendance.get();
            attendance.setWinner(attendanceDTO.isWinner());
            attendance.setScore(attendanceDTO.getScore());

            // Save the updated attendance
            Attendance updatedAttendance = attendanceDAO.updateAttendance(attendance);

            // Convert and return the updated attendance
            return convertToDTO(updatedAttendance);
        } else {
            throw new IllegalArgumentException("Attendance not found with id: " + id);
        }
    }
```

#### How this Test Works:

This test verifies that the **updateAttendance** method in **AttendanceService** correctly updates an existing attendance record in the **Attendance** table. Here's a detailed breakdown:

**New attendance data to set:**
`attendanceId` is set to `1L`, representing the ID of the attendance entry to be updated.
`attendanceDTO` is a mock object simulating new data for the attendance entry. 
- The new score is `15`.
- The winner flag is set to `true`.

**Existing Attendance:**
`attendance` is another temproray mock object representing a virtual current state of the attendance entry in the database.
- The current score is `10`.
- The winner flag is `false`.

The `when` methods configure the behavior of the mocked `attendanceDAO`:
**Find by ID:** When `attendanceDAO.findById(attendanceId)` is called, it returns the mock `attendance` object wrapped in `Optional.of()`.
**Update Attendance:** When `attendanceDAO.updateAttendance` is called with any `Attendance` object, it returns the same object, simulating a successful database update.

The test calls `attendanceService.updateAttendance(attendanceId, attendanceDTO)`.
Inside the `updateAttendance` method:
1. The `attendanceDAO.findById` fetches the existing attendance.
2. The attendance's properties (`score` and `winner`) are updated with values from `attendanceDTO`.
3. The updated attendance is saved using `attendanceDAO.updateAttendance`.
4. The updated attendance is converted to a `AttendanceDTO` and returned.


The test verifies that:
**The result is not null:** Ensures the service returns a valid object.
**Score is updated correctly:** The score in the result matches the value in `attendanceDTO` (15).
**Winner flag is updated correctly:** The winner flag in the result matches the value in `attendanceDTO` (true).


`verify` ensures the mocks were called the expected number of times:
- `attendanceDAO.findById` was called **once** to fetch the attendance.
- `attendanceDAO.updateAttendance` was called **once** to save the updated attendance.


This test ensures that:
- The correct attendance record is fetched from the database.
- Its properties are updated with the new values.
- The updated record is saved.
- The method behaves as expected, returning the updated data as a DTO.

---

# Conclusion

This project was an enjoyable and insightful introduction to Spring Boot, JPA functionalities, and the practical implementation of REST API communication between two separate projects. While there is still room for improvement, the backend establishes a good starting foundation for managing databases and allows for future tweaks and adjustments to better align with user requirements.

That said, some constraints, particularly in the Game module, could be enhanced. 
Additionally, a few methods in the Friend microservice and the REST API calls in the Attendance microservice did not pass all tests and remain areas for further refinement. Overall, this project serves as a starting point with potential for optimization and expansion throughout the fullstack process.
