package server;

import service.GameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    final GameService gameService;
    public CreateGameHandler(Object gameService) {
        this.gameService = (GameService) gameService;
    }
    public Object createGame(Request request, Response response) {
        return null;
    }
}
