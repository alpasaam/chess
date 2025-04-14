package server.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;
import websocket.commands.MakeMoveCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final Gson gson = new Gson();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getPlayerName(), command.isObserver(), session);
            case MAKE_MOVE -> {
                MakeMoveCommand moveCommand = gson.fromJson(message, MakeMoveCommand.class);
                makeMove(moveCommand.getGameId(), moveCommand.getMove(), moveCommand.getPlayerName());
            }
            case LEAVE -> leave(command.getPlayerName());
            case RESIGN -> resign(command.getGameId(), command.getPlayerName());
            default -> throw new IllegalArgumentException("Unknown command type: " + command.getCommandType());
        }
    }

    private void connect(String playerName, boolean isObserver, Session session) throws IOException {
        connections.add(playerName, session);
        String message = isObserver
                ? String.format("%s joined as an observer", playerName)
                : String.format("%s joined the game", playerName);
        ServerMessage serverMessage = new ServerMessage(ServerMessage.Type.CONNECT, message);
        connections.broadcast(playerName, serverMessage);
    }

    private void makeMove(String gameId, String move, String playerName) throws IOException {
        String message = String.format("%s made a move in game %s: %s", playerName, gameId, move);
        ServerMessage serverMessage = new ServerMessage(ServerMessage.Type.MOVE, message);
        connections.broadcast(playerName, serverMessage);
    }

    private void leave(String playerName) throws IOException {
        connections.remove(playerName);
        String message = String.format("%s left the game", playerName);
        ServerMessage serverMessage = new ServerMessage(ServerMessage.Type.LEAVE, message);
        connections.broadcast(playerName, serverMessage);
    }

    private void resign(String gameId, String playerName) throws IOException {
        String message = String.format("%s resigned from game %s", playerName, gameId);
        ServerMessage serverMessage = new ServerMessage(ServerMessage.Type.RESIGN, message);
        connections.broadcast(playerName, serverMessage);
    }
}