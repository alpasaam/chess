package server;

import dataaccess.DataAccessException;
import exception.ResponseException;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    final UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    public Object logout(Request req, Response res) throws ResponseException {
        // call the logout method of the UserService
        userService.logout(req.headers("authorization"));
        // return an empty string
        return "{}";
    }
}
