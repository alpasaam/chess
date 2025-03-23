package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.*;

import java.util.Collection;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Collection<GameData> listGame(String authorization) throws ResponseException {
        if (authDAO.getAuth(authorization) == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        return gameDAO.listGames();
    }

    public NewGameResponse createGame(NewGameRequest newGameRequest) throws ResponseException {
        String authToken = newGameRequest.authToken();
        String gameName = newGameRequest.gameName();
        if (authDAO.getAuth(authToken) == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        GameData gameData = new GameData(0, null, null, gameName, null);
        gameData = gameDAO.createGame(gameData);
        return new NewGameResponse(gameData.gameID());
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        String authToken = joinGameRequest.authToken();
        String playerColor = joinGameRequest.playerColor();
        int gameID = joinGameRequest.gameID();
        AuthData authData = authDAO.getAuth(authToken);
        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null || playerColor == null || !playerColor.equals("WHITE") && !playerColor.equals("BLACK")) {
            throw new ResponseException(400, "Error: bad request");
        }
        if (authToken == null || authData == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        if (playerColor.equals("WHITE") && gameData.whiteUsername() != null || playerColor.equals("BLACK") && gameData.blackUsername() != null) {
            throw new ResponseException(403, "Error: already taken");
        }
        if (playerColor.equals("WHITE")) {
            gameDAO.updateGame(new GameData(gameID, authData.username(), gameData.blackUsername(), gameData.gameName(), new ChessGame()));
        } else {
            gameDAO.updateGame(new GameData(gameID, gameData.whiteUsername(), authData.username(), gameData.gameName(), new ChessGame()));
        }
    }

    public void clear() {
        try {
            gameDAO.clear();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }
}
