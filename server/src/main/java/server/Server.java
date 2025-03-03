package server;

import dataaccess.*;
import service.UserService;
import spark.*;

public class Server {

    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        RegisterHandler registerHandler = new RegisterHandler(new UserService(userDAO, authDAO));
        LoginHandler loginHandler = new LoginHandler(new UserService(userDAO, authDAO));
        LogoutHandler logoutHandler = new LogoutHandler(new UserService(userDAO, authDAO));

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.post("/user", registerHandler::register);
        Spark.post("/session", loginHandler::login);
        Spark.delete("/session", logoutHandler::logout);



        Spark.delete("/db", this::clear);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clear(Request request, Response response) {
        userDAO.clear();
        authDAO.clear();
        return "{}";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
