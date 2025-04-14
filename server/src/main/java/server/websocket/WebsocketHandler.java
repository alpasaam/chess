package server.websocket;

import com.google.gson.Gson;
import dataaccess.GameDAO;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;
import websocket.commands.MakeMoveCommand;
import dataaccess.AuthDAO;

import java.io.IOException;

@WebSocket
public class WebsocketHandler {

    AuthDAO authDAO;
    GameDAO gameDAO;

    public WebsocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    private final ConnectionManager connections = new ConnectionManager();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws ResponseException, IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        if (command.getCommandType() == null) {
            ServerMessage errorMessage = new ErrorMessage("ERROR: Command type is null");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }
        if (command.getAuthToken() == null) {
            ServerMessage errorMessage = new ErrorMessage("ERROR: Authentication token is null");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }
        if (authDAO.getAuth(command.getAuthToken())== null){
            ServerMessage errorMessage = new ErrorMessage("ERROR: Invalid authentication token");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }
        if (command.getGameID() == null) {
            ServerMessage errorMessage = new ErrorMessage("ERROR: Game ID is null");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }
        String username = authDAO.getAuth(command.getAuthToken()).username();

        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session, username);
            case MAKE_MOVE -> makeMove(message, username);
            case LEAVE -> leave(command, username);
            case RESIGN -> resign(command, username);
            default -> throw new IllegalArgumentException("Unknown command type: " + command.getCommandType());
        }
    }

    private void connect(UserGameCommand command, Session session, String username) throws IOException, ResponseException {
        // Validate authentication
        var authData = authDAO.getAuth(command.getAuthToken());
        if (authData == null) {
            ServerMessage errorMessage = new ErrorMessage("ERROR: Invalid authentication token");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        // Validate game data
        var gameData = gameDAO.getGame(command.getGameID());
        if (gameData == null) {
            ServerMessage errorMessage = new ErrorMessage("ERROR: Game not found for ID: " + command.getGameID());
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        connections.add(username, session, command.getGameID());

        String role;
        if (gameData.whiteUsername().equals(username)) {
            role = "white";
        } else if (gameData.blackUsername().equals(username)) {
            role = "black";
        } else {
            role = "observer";
        }

        var game = gameDAO.getGame(command.getGameID());
        ServerMessage loadGameMessage = new LoadGameMessage(game);
        session.getRemote().sendString(new Gson().toJson(loadGameMessage));

        String message = String.format("%s joined the game as %s", username, role);
        ServerMessage notificationMessage = new NotificationMessage(message);
        connections.broadcastToGame(command.getGameID(), notificationMessage, username);
    }

    private void makeMove(String message, String username) throws IOException {
        MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
        String moveMessage = String.format("%s made a move in game %s: %s", username, moveCommand.getGameID(), moveCommand.getMove());
        ServerMessage serverMessage = new NotificationMessage(moveMessage);
        connections.broadcastToGame(moveCommand.getGameID(), serverMessage, username);
    }

    private void leave(UserGameCommand command, String username) throws IOException {
        connections.remove(username);
        String message = String.format("%s left the game", username);
        ServerMessage serverMessage = new NotificationMessage(message);
        connections.broadcastToGame(command.getGameID(), serverMessage, username);
    }

    private void resign(UserGameCommand command, String username) throws IOException {
        String message = String.format("%s resigned from the game", username);
        ServerMessage serverMessage = new NotificationMessage(message);
        connections.broadcastToGame(command.getGameID(), serverMessage, username);
    }
}