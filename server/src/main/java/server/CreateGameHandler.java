package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.NewGameRequest;
import service.GameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    final GameService gameService;
    public CreateGameHandler(Object gameService) {
        this.gameService = (GameService) gameService;
    }


    public Object createGame(Request request, Response response) throws ResponseException {
        GameData gamedata = new Gson().fromJson(request.body(), GameData.class);
        String authToken = request.headers("authorization");
        NewGameRequest newGameRequest = new NewGameRequest(authToken, gamedata.gameName());

        return new Gson().toJson(gameService.createGame(newGameRequest));
    }
}
