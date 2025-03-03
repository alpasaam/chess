package server;

import dataaccess.DataAccessException;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    final UserService userService;

    public LogoutHandler(UserService userService) {
        this.userService = userService;
    }

    public Object logout(Request req, Response res) throws DataAccessException {
        // call the logout method of the UserService
        userService.logout(req.headers("authorization"));
        // return an empty string
        return "{}";
    }
}
