package server;

import dataaccess.*;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    final UserDAO userDAO = new MemoryUserDAO();
    final AuthDAO authDAO = new MemoryAuthDAO();
    final GameDAO gameDAO = new MemoryGameDAO();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

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

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clear(Request request, Response response) {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
        return "{}";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
