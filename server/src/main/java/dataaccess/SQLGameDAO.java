package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO{
    @Override
    public GameData createGame(GameData gameData) {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public GameData getGame(int id) {
        return null;
    }

    @Override
    public void updateGame(GameData gameData) {

    }

    @Override
    public void clear() {

    }
}
