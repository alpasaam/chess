package client;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.LoginRequest;
import model.RegisterRequest;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void clear() throws ResponseException {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerPositive() throws ResponseException {
        var authData = facade.register(new RegisterRequest ("player1", "password", "p1@email.com"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void registerNegative() throws ResponseException {
        var authData = facade.register(new RegisterRequest ("", "", ""));
        assertThrows(ResponseException.class, () -> facade.register(new RegisterRequest ("", "", "")));
    }

    @Test
    public void loginPositive() throws ResponseException {
        facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        var loginResponse = facade.login(new LoginRequest("player1", "password"));
        assertTrue(loginResponse.authToken().length() > 10);
    }

    @Test
    public void loginNegative() throws ResponseException {
        facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        assertThrows(ResponseException.class, () -> facade.login(new LoginRequest("player1", "wrongPassword")));
    }

    @Test
    public void logoutPositive() throws ResponseException{
        AuthDAO authDAO = new SQLAuthDAO();
        String authToken = "validAuth";
        authDAO.createAuth(new AuthData("username", authToken));
        facade.logout(authToken);
        assertNull(authDAO.getAuth(authToken));
    }

    @Test
    public void logoutNegative() throws ResponseException{
        String invalidAuthToken = "invalidAuthToken";
        assertThrows(ResponseException.class, () -> facade.logout(invalidAuthToken));
    }

    @Test
    public void listGamesPositive() throws ResponseException{
        AuthDAO authDAO = new SQLAuthDAO();
        GameDAO gameDAO = new SQLGameDAO();
        String authorization = "validAuthToken";
        authDAO.createAuth(new AuthData("username", authorization));
        gameDAO.createGame(new GameData(1, "whitePlayer", "blackPlayer", "gameName", new ChessGame()));

        Collection<GameData> result = facade.listGames(authorization);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void listGamesNegative() throws ResponseException{
        String invalidAuthorization = "invalidAuthToken";
        assertThrows(ResponseException.class, () -> facade.listGames(invalidAuthorization));
    }

    @Test
    public void createGamePositive() throws ResponseException{

    }

    @Test
    public void createGameNegative() throws ResponseException{

    }

    @Test
    public void joinGamePositive() throws ResponseException{

    }

    @Test
    public void joinGameNegative() throws ResponseException{

    }


}
