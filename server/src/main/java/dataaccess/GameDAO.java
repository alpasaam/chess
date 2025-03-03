package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData createGame(GameData gameData);
    Collection<GameData> listGames();
    GameData getGame(int id);
    void clear();
}
