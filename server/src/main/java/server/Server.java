package server;

import dataaccess.*;
import exception.ResponseException;
import server.websocket.WebsocketHandler;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Spark;


public class Server {

    final UserDAO userDAO;
    final AuthDAO authDAO;
    final GameDAO gameDAO;

    {
        try {
            userDAO = new SQLUserDAO();
            authDAO = new SQLAuthDAO();
            gameDAO = new SQLGameDAO();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        WebsocketHandler websocketHandler = new WebsocketHandler(authDAO, gameDAO);

        Spark.webSocket("/ws", websocketHandler);

        // Register your endpoints and handle exceptions here.
        RegisterHandler registerHandler = new RegisterHandler(new UserService(userDAO, authDAO));
        LoginHandler loginHandler = new LoginHandler(new UserService(userDAO, authDAO));
        LogoutHandler logoutHandler = new LogoutHandler(new UserService(userDAO, authDAO));
        CreateGameHandler createGameHandler = new CreateGameHandler(new GameService(gameDAO, authDAO));
        JoinGameHandler joinGameHandler = new JoinGameHandler(new GameService(gameDAO, authDAO));
        ListGameHandler listGameHandler = new ListGameHandler(new GameService(gameDAO, authDAO));
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.post("/user", registerHandler::register);
        Spark.post("/session", loginHandler::login);
        Spark.delete("/session", logoutHandler::logout);

        Spark.get("/game", listGameHandler::listGames);
        Spark.post("/game", createGameHandler::createGame);
        Spark.put("/game", joinGameHandler::joinGame);


        Spark.delete("/db", this::clear);

        Spark.exception(ResponseException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public Object clear(Request request, Response response) throws ResponseException {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
        return "{}";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.statusCode());
        res.body(ex.toJson());
    }
}
