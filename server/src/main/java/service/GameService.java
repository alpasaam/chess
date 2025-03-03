package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import model.NewGameRequest;
import model.NewGameResponse;

import java.util.Collection;
import java.util.ListIterator;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Collection<GameData> listGame(String authorization) throws DataAccessException {
        authDAO.getAuth(authorization);
        return gameDAO.listGames();
    }

    public NewGameResponse createGame(NewGameRequest newGameRequest) throws DataAccessException {
        String authToken = newGameRequest.authToken();
        String gameName = newGameRequest.gameName();
        authDAO.getAuth(authToken);
        GameData gameData = new GameData(0, "", "", gameName, null);
        gameData = gameDAO.createGame(gameData);
        return new NewGameResponse(gameData.gameID());
    }
}
