package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.GameService;
import spark.Request;
import spark.Response;

public class ListGameHandler {
    final GameService gameService;

    public ListGameHandler(Object gameService) {
        this.gameService = (GameService) gameService;
    }

    public Object listGames(Request request, Response response) throws DataAccessException {
        return new Gson().toJson(gameService.listGame(request.headers("authorization")));
    }
}
