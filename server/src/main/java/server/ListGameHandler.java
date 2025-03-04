package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.ListGameResponse;
import service.GameService;
import spark.Request;
import spark.Response;

public class ListGameHandler {
    final GameService gameService;

    public ListGameHandler(Object gameService) {
        this.gameService = (GameService) gameService;
    }

    public Object listGames(Request request, Response response) throws ResponseException {
        String authorization = request.headers("authorization");
        ListGameResponse listGameResponse = new ListGameResponse(gameService.listGame(authorization));
        return new Gson().toJson(listGameResponse);
    }
}
