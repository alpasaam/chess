package service;

import chess.ChessGame;
import dataaccess.*;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private GameService gameService;
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();

    @BeforeEach
    void setUp() {
        gameService = new GameService(gameDAO, authDAO);
    }

    @AfterEach
    void tearDown() {
    }

    // Positive test case for listGame
    @Test
    void listGame_Positive() throws ResponseException {
        String authorization = "validAuthToken";
        authDAO.createAuth(new AuthData("username", authorization));
        gameDAO.createGame(new GameData(1, "whitePlayer", "blackPlayer", "gameName", new ChessGame()));

        Collection<GameData> result = gameService.listGame(authorization);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    // Negative test case for listGame
    @Test
    void listGame_Negative() {
        String invalidAuthorization = "invalidAuthToken";
        assertThrows(ResponseException.class, () -> {
            gameService.listGame(invalidAuthorization);
        });
    }

    // Positive test case for createGame
    @Test
    void createGame_Positive() throws ResponseException {
        String authToken = "validAuthToken";
        String gameName = "newGame";
        NewGameRequest newGameRequest = new NewGameRequest(authToken, gameName);
        authDAO.createAuth(new AuthData("username", authToken));

        NewGameResponse result = gameService.createGame(newGameRequest);

        assertNotNull(result);
        assertEquals(1, result.gameID());
    }

    // Negative test case for createGame
    @Test
    void createGame_Negative() {
        String invalidAuthToken = "invalidAuthToken";
        String gameName = "newGame";
        NewGameRequest newGameRequest = new NewGameRequest(invalidAuthToken, gameName);
        assertThrows(ResponseException.class, () -> {
            gameService.createGame(newGameRequest);
        });
    }

    // Positive test case for joinGame
    @Test
    void joinGame_Positive() throws ResponseException {
        String authToken = "validAuthToken";
        String playerColor = "WHITE";
        int gameID = 1;
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, playerColor, gameID);
        authDAO.createAuth(new AuthData("username", authToken));
        gameDAO.createGame(new GameData(gameID, null, null, "gameName", new ChessGame()));

        gameService.joinGame(joinGameRequest);

        GameData updatedGame = gameDAO.getGame(gameID);
        assertNotNull(updatedGame);
        assertEquals("username", updatedGame.whiteUsername());
    }

    @Test
    void joinGame_Negative() {
        String invalidAuthToken = "invalidAuthToken";
        String playerColor = "WHITE";
        int gameID = 1;
        JoinGameRequest joinGameRequest = new JoinGameRequest(invalidAuthToken, playerColor, gameID);
        assertThrows(ResponseException.class, () -> {
            gameService.joinGame(joinGameRequest);
        });
    }

    // Positive test case for clear
    @Test
    void clear_Positive() {
        gameDAO.createGame(new GameData(1, "whitePlayer", "blackPlayer", "gameName", new ChessGame()));

        gameService.clear();

        assertNull(gameDAO.getGame(1));
    }

}