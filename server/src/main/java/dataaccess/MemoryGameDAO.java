package dataaccess;

import exception.ResponseException;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private int nextID = 1;
    private final HashMap<Integer, GameData> games = new HashMap<>();

    public GameData createGame(GameData gameData) throws ResponseException {
        gameData = new GameData(nextID++, gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), gameData.game());

        games.put(gameData.gameID(), gameData);
        return gameData;
    }

    public Collection<GameData> listGames() throws ResponseException {
        return games.values();
    }


    public GameData getGame(int id) throws ResponseException {
        return games.get(id);
    }

    public void clear() throws ResponseException {
        games.clear();
        nextID = 1;
    }

    @Override
    public void updateGame(GameData gameData) throws ResponseException {
        games.put(gameData.gameID(), gameData);
    }

}
