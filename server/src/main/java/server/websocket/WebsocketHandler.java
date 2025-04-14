package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebsocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    AuthDAO authDAO;
    GameDAO gameDAO;

    public WebsocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

            if (command.getCommandType() == null) {
                throw new ResponseException(400, "Command type is null");
            }
            if (command.getAuthToken() == null) {
                throw new ResponseException(400, "Authentication token is null");
            }
            var authData = authDAO.getAuth(command.getAuthToken());
            if (authData == null) {
                throw new ResponseException(401, "Invalid authentication token");
            }
            if (command.getGameID() == null) {
                throw new ResponseException(400, "Game ID is null");
            }
            var gameData = gameDAO.getGame(command.getGameID());
            if (gameData == null) {
                throw new ResponseException(404, "Game not found");
            }

            String username = authData.username();

            switch (command.getCommandType()) {
                case CONNECT -> connect(command, session, username, gameData);
                case MAKE_MOVE -> makeMove(message, username);
                case LEAVE -> leave(command, username);
                case RESIGN -> resign(command, username, gameData);
                default -> throw new IllegalArgumentException("Unknown command type: " + command.getCommandType());
            }
        } catch (Exception e) {
            ServerMessage errorMessage = new ErrorMessage("ERROR: " + e.getMessage());
            session.getRemote().sendString(new Gson().toJson(errorMessage));
        }
    }

    private void connect(UserGameCommand command, Session session, String username, GameData gameData) throws IOException {
        connections.add(username, session, command.getGameID());

        String role;
        if (gameData.whiteUsername().equals(username)) {
            role = "white";
        } else if (gameData.blackUsername().equals(username)) {
            role = "black";
        } else {
            role = "observer";
        }

        // Use broadcastToUser to send the LoadGameMessage
        ServerMessage loadGameMessage = new LoadGameMessage(gameData);
        connections.broadcastToUser(username, loadGameMessage);

        String message = String.format("%s joined the game as %s", username, role);
        ServerMessage notificationMessage = new NotificationMessage(message);
        connections.broadcastToGame(command.getGameID(), notificationMessage, username);
    }

    private void makeMove(String message, String username) throws IOException, ResponseException {
        MakeMoveCommand moveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
        GameData gameData = gameDAO.getGame(moveCommand.getGameID());
        if (gameData == null) {
            throw new ResponseException(404, "Game not found for ID: " + moveCommand.getGameID());
        }

        ChessGame chessGame = gameData.game();

        ChessGame.TeamColor playerTeam = gameData.whiteUsername().equals(username) ? ChessGame.TeamColor.WHITE
                : gameData.blackUsername().equals(username) ? ChessGame.TeamColor.BLACK : null;

        if (playerTeam == null) {
            throw new ResponseException(403, "You are not a player in this game");
        }

        if (playerTeam != chessGame.getTeamTurn()) {
            throw new ResponseException(400, "It is not your turn");
        }

        ChessMove move = moveCommand.getMove();
        try {
            chessGame.makeMove(move);
        } catch (InvalidMoveException e) {
            throw new ResponseException(400, "Invalid move");
        }

        gameDAO.updateGame(gameData);

        ServerMessage loadGameMessage = new LoadGameMessage(gameData);
        connections.broadcastToGame(moveCommand.getGameID(), loadGameMessage, null);

        String moveDescription = String.format("%s to %s", move.getStartPosition(), move.getEndPosition());
        String moveMessage = String.format("%s made a move: %s", username, moveDescription);
        ServerMessage notificationMessage = new NotificationMessage(moveMessage);
        connections.broadcastToGame(moveCommand.getGameID(), notificationMessage, username);
    }

    private void leave(UserGameCommand command, String username) throws IOException, ResponseException {
        GameData gameData = gameDAO.getGame(command.getGameID());
        if (gameData == null) {
            throw new ResponseException(404, "Game not found");
        }

        boolean isWhitePlayer = username.equals(gameData.whiteUsername());
        boolean isBlackPlayer = username.equals(gameData.blackUsername());

        if (!isWhitePlayer && !isBlackPlayer) {
            connections.remove(username);
        } else {
            ChessGame.TeamColor teamTurn = gameData.game().getTeamTurn();
            if (isWhitePlayer) {
                gameData = new GameData(gameData.gameID(), null, gameData.blackUsername(), gameData.gameName(), gameData.game());
            } else {
                gameData = new GameData(gameData.gameID(), gameData.whiteUsername(), null, gameData.gameName(), gameData.game());
            }

            if (gameData.whiteUsername() == null && gameData.blackUsername() == null) {
                gameData.game().setTeamTurn(ChessGame.TeamColor.NONE);
            }

            gameDAO.updateGame(gameData);

            connections.remove(username);
        }

        String message = String.format("%s left the game", username);
        ServerMessage serverMessage = new NotificationMessage(message);
        connections.broadcastToGame(command.getGameID(), serverMessage, username);
    }

    private void resign(UserGameCommand command, String username, GameData gameData) throws IOException, ResponseException {
        if (!gameData.whiteUsername().equals(username) && !gameData.blackUsername().equals(username)) {
            throw new ResponseException(403, "You are not a player in this game");
        }

        if (gameData.game().getTeamTurn().equals(ChessGame.TeamColor.NONE)) {
            throw new ResponseException(400, "The game is already over");
        }

        gameData.game().setTeamTurn(ChessGame.TeamColor.NONE);
        gameDAO.updateGame(gameData);

        String message = String.format("%s resigned from the game", username);
        ServerMessage notificationMessage = new NotificationMessage(message);
        connections.broadcastToGame(command.getGameID(), notificationMessage, null);
    }
}