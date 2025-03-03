package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.NewGameRequest;
import model.NewGameResponse;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public NewGameResponse createGame(NewGameRequest newGameRequest) throws DataAccessException {
        String authToken = newGameRequest.authToken();
        String gameName = newGameRequest.gameName();
        authDAO.getAuth(authToken);

        return null;
    }
}
