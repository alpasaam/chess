package dataaccess;

import exception.ResponseException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData createGame(GameData gameData) throws ResponseException;
    Collection<GameData> listGames() throws ResponseException;
    GameData getGame(int id) throws ResponseException;
    void updateGame(GameData gameData) throws ResponseException;
    void clear() throws ResponseException;
}
