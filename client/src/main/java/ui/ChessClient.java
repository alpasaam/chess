package ui;

import chess.ChessMove;
import exception.ResponseException;
import model.*;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;

import java.util.Collection;

public class ChessClient {
    private final ServerFacade serverFacade;
    private final String serverUrl;
    private final NotificationHandler notificationHandler;
    private WebSocketFacade webSocketFacade;

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        this.serverFacade = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    // ServerFacade methods
    public LoginResponse login(String username, String password) throws ResponseException {
        return serverFacade.login(new LoginRequest(username, password));
    }

    public RegisterResponse register(String username, String password, String email) throws ResponseException {
        return serverFacade.register(new RegisterRequest(username, password, email));
    }

    public void logout(String authToken) throws ResponseException {
        serverFacade.logout(authToken);
    }

    public Collection<GameData> listGames(String authToken) throws ResponseException {
        return serverFacade.listGames(authToken);
    }

    public void createGame(String gameName, String authToken) throws ResponseException {
        serverFacade.createGame(new NewGameRequest(authToken, gameName));
    }

    public void joinGame(String authToken, String color, int gameId) throws ResponseException {
        serverFacade.joinGame(new JoinGameRequest(authToken, color, gameId));
    }

    // WebSocketFacade methods
    public void connect(String authToken, int gameID) throws ResponseException {
        webSocketFacade.connect(authToken, gameID);
    }

    public void leave(String authToken, int gameID) throws ResponseException {
        webSocketFacade.leave(authToken, gameID);
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException {
        webSocketFacade.makeMove(authToken, gameID, move);
    }

    public void resign(String authToken, int gameID) throws ResponseException {
        webSocketFacade.resign(authToken, gameID);
    }

    public void setWebSocketFacade(WebSocketFacade webSocketFacade) {
        this.webSocketFacade = webSocketFacade;
    }
}