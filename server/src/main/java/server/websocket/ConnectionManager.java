package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String visitorName, Session session, int gameID) {
        var connection = new Connection(visitorName, session, gameID);
        connections.put(visitorName, connection);
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public void broadcastToGame(int gameID, ServerMessage serverMessage, String currentUserName) throws IOException {
        var removeList = new ArrayList<Connection>();
        String jsonMessage = new Gson().toJson(serverMessage); // Serialize the ServerMessage to JSON
        for (var connection : connections.values()) {
            if (connection.session.isOpen()) {
                if (connection.gameID == gameID && !connection.visitorName.equals(currentUserName)) {
                    connection.send(jsonMessage); // Send the serialized JSON
                }
            } else {
                removeList.add(connection);
            }
        }

        // Clean up any closed connections
        for (var connection : removeList) {
            connections.remove(connection.visitorName);
        }
    }

    public void broadcastToAll(ServerMessage serverMessage) throws IOException {
        var removeList = new ArrayList<Connection>();
        String jsonMessage = new Gson().toJson(serverMessage); // Serialize the ServerMessage to JSON
        for (var connection : connections.values()) {
            if (connection.session.isOpen()) {
                connection.send(jsonMessage); // Send the serialized JSON
            } else {
                removeList.add(connection);
            }
        }

        for (var connection : removeList) {
            connections.remove(connection.visitorName);
        }
    }

    public void broadcastToUser(String username, ServerMessage serverMessage) throws IOException {
        var connection = connections.get(username);
        if (connection != null) {
            if (connection.session.isOpen()) {
                String jsonMessage = new Gson().toJson(serverMessage); // Serialize the ServerMessage to JSON
                connection.send(jsonMessage); // Send the serialized JSON
            } else {
                connections.remove(username); // Remove the connection if the session is closed
            }
        }
    }
}