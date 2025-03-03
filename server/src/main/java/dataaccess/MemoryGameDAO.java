package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private int nextID = 1;
    private final HashMap<Integer, GameData> games = new HashMap<>();

    public GameData createGame(GameData gameData){
        gameData = new GameData(nextID++, gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), gameData.game());

        games.put(gameData.gameID(), gameData);
        return gameData;
    }

    public Collection<GameData> listGames() {
        return games.values();
    }


    public GameData getGame(int id) {
        return games.get(id);
    }

    public void clear() {
        games.clear();
    }

}
