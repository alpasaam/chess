package server.websocket;

import com.google.gson.Gson;
import dataaccess.GameDAO;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;
import websocket.commands.MakeMoveCommand;
import dataaccess.AuthDAO;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    AuthDAO authDAO;
    GameDAO gameDAO;

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    private final ConnectionManager connections = new ConnectionManager();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws ResponseException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        String username = authDAO.getAuth(command.getAuthToken()).username();

        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session, username);
            case MAKE_MOVE -> makeMove(message, username);
            case LEAVE -> leave(command, username);
            case RESIGN -> resign(command, username);
            default -> throw new IllegalArgumentException("Unknown command type: " + command.getCommandType());
        }
    }

    private void connect(UserGameCommand command, Session session, String username) throws ResponseException {
        connections.add(username, session, command.getGameID());
        String message = command.isObserver()
                ? String.format("%s joined as an observer", username)
                : String.format("%s joined the game", username);
        ServerMessage serverMessage = new NotificationMessage(message);
        connections.broadcastToGame(command.getGameID(), serverMessage);
    }

    private void makeMove(String message, String username) throws IOException {
        MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
        String moveMessage = String.format("%s made a move in game %s: %s", username, moveCommand.getGameID(), moveCommand.getMove());
        ServerMessage serverMessage = new NotificationMessage(moveMessage);
        connections.broadcastToGame(moveCommand.getGameID(), serverMessage);
    }

    private void leave(UserGameCommand command, String username) throws IOException {
        connections.remove(username);
        String message = String.format("%s left the game", username);
        ServerMessage serverMessage = new NotificationMessage(message);
        connections.broadcastToGame(command.getGameID(), serverMessage);
    }

    private void resign(UserGameCommand command, String username) throws IOException {
        String message = String.format("%s resigned from game %s", username, command.getGameID());
        ServerMessage serverMessage = new NotificationMessage(message);
        connections.broadcastToGame(command.getGameID(), serverMessage);
    }
}