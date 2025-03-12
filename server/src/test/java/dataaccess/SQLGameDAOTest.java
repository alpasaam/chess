package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTest {

    private SQLGameDAO gameDAO;

    @BeforeEach
    void setUp() throws ResponseException {
        gameDAO = new SQLGameDAO();
        gameDAO.clear();
    }

    @Test
    void createGame_Positive() throws ResponseException {
        GameData gameData = new GameData(1, "whitePlayer", "blackPlayer", "Test Game", new ChessGame());
        GameData createdGame = gameDAO.createGame(gameData);
        assertNotNull(createdGame, "Game creation failed, returned null");
        assertEquals(gameData.whiteUsername(), createdGame.whiteUsername(), "White player username mismatch");
        assertEquals(gameData.blackUsername(), createdGame.blackUsername(), "Black player username mismatch");
        assertEquals(gameData.gameName(), createdGame.gameName(), "Game name mismatch");
    }

    @Test
    void createGame_Negative() {
        GameData gameData = new GameData(1, "whitePlayer", "blackPlayer", null, new ChessGame());
        ResponseException exception = assertThrows(ResponseException.class, () -> gameDAO.createGame(gameData));
        assertEquals(400, exception.statusCode(), "Expected status code 400");
    }

    @Test
    void listGames_Positive() throws ResponseException {
        GameData gameData1 = new GameData(1, "whitePlayer1", "blackPlayer1", "Test Game 1", new ChessGame());
        GameData gameData2 = new GameData(2, "whitePlayer2", "blackPlayer2", "Test Game 2", new ChessGame());
        gameDAO.createGame(gameData1);
        gameDAO.createGame(gameData2);
        Collection<GameData> games = gameDAO.listGames();
        assertEquals(2, games.size());
    }

    @Test
    void listGames_Negative() throws ResponseException {
        Collection<GameData> games = gameDAO.listGames();
        assertEquals(0, games.size());
    }

    @Test
    void getGame_Positive() throws ResponseException {
        GameData gameData = new GameData(0, "whitePlayer", "blackPlayer", "Test Game", new ChessGame());
        GameData game = gameDAO.createGame(gameData);
        int gameID = game.gameID();
        GameData retrievedGame = gameDAO.getGame(gameID);
        assertNotNull(retrievedGame);
        assertEquals(gameID, retrievedGame.gameID());
    }

    @Test
    void getGame_Negative() throws ResponseException {
        GameData gameData = gameDAO.getGame(999);
        assertNull(gameData);
    }

    @Test
    void updateGame_Positive() throws ResponseException {
        GameData gameData = new GameData(1, "whitePlayer", "blackPlayer", "Test Game", new ChessGame());
        GameData game = gameDAO.createGame(gameData);
        int gameID = game.gameID();
        gameData = new GameData(gameID, "newWhitePlayer", "newBlackPlayer", "New Test Game", new ChessGame());
        gameDAO.updateGame(gameData);
        GameData updatedGame = gameDAO.getGame(gameID);
        assertEquals(gameData.whiteUsername(), updatedGame.whiteUsername(), "White player username mismatch");
        assertEquals(gameData.blackUsername(), updatedGame.blackUsername(), "Black player username mismatch");
        assertEquals(gameData.gameName(), updatedGame.gameName(), "Game name mismatch");
    }

    @Test
    void updateGame_Negative() {
        GameData gameData = null;
        assertThrows(NullPointerException.class, () -> gameDAO.updateGame(gameData));
    }

    @Test
    void clear_Positive() throws ResponseException {
        GameData gameData = new GameData(1, "whitePlayer", "blackPlayer", "Test Game", new ChessGame());
        gameDAO.createGame(gameData);
        gameDAO.clear();
        GameData retrievedGame = gameDAO.getGame(1);
        assertNull(retrievedGame);
    }
}