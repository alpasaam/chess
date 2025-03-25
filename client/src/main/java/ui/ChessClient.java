package ui;

import chess.ChessBoard;
import exception.ResponseException;
import model.*;
import server.ServerFacade;

import java.util.Collection;

import static java.lang.System.out;
import static ui.GamePlayUI.drawChessBoard;

public class ChessClient {
    private final ServerFacade server;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void start() {
        PreloginUI preloginUI = new PreloginUI(this);
        preloginUI.run();
    }

    public LoginResponse login(String username, String password) throws ResponseException {
        return server.login(new LoginRequest(username, password));
    }

    public RegisterResponse register(String username, String password, String email) throws ResponseException {
        return server.register(new RegisterRequest(username, password, email));
    }

    public void logout(String authToken) throws ResponseException {
        server.logout(authToken);
    }

    public void createGame(String gameName, String authToken) throws ResponseException {
        server.createGame(new NewGameRequest(authToken, gameName));
    }

    public Collection<GameData> listGames(String authToken) throws ResponseException {
        return server.listGames(authToken);
    }

    public void joinGame(String authToken, String color, int gameId) throws ResponseException {
        server.joinGame(new JoinGameRequest(authToken, color, gameId));
    }

    public void observeGame(int gameId, String authToken) throws ResponseException {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        drawChessBoard(out, true, board);
    }

}