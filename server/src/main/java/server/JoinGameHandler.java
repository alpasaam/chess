package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.JoinGameRequest;
import service.GameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    final GameService gameService;
    public JoinGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object joinGame(Request req, Response resp) throws DataAccessException {
        JoinGameRequest joinGameRequest = new JoinGameRequest(
                req.headers("authorization"),
                new Gson().fromJson(req.body(), JoinGameRequest.class).playerColor(),
                new Gson().fromJson(req.body(), JoinGameRequest.class).gameID()
        );
        gameService.joinGame(joinGameRequest);
        return "{}";
    }
}
