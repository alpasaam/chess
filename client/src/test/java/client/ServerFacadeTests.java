package client;

import exception.ResponseException;
import model.LoginRequest;
import model.RegisterRequest;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
        var authData = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        var loginResponse = facade.login(new LoginRequest("player1", "password"));
        assertTrue(loginResponse.authToken().length() > 10);
    }

    @Test
    public void loginNegative() throws ResponseException {
        var authData = facade.register(new RegisterRequest("player1", "password", "p1@email.com"));
        assertThrows(ResponseException.class, () -> facade.login(new LoginRequest("player1", "wrongPassword")));
    }
}
