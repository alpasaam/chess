package server;

import service.GameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    final GameService gameService;
    public JoinGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object joinGame(Request request, Response response) {
        return null;
    }
}
