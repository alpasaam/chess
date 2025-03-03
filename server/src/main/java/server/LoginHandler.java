package server;

import com.google.gson.Gson;
import model.LoginRequest;
import model.LoginResponse;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class LoginHandler {

    UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    public Object login(Request req, Response res) {
        // parse the request body into a UserData object
        UserData userData = new Gson().fromJson(req.body(), UserData.class);
        // create a LoginRequest object
        LoginRequest loginRequest = new LoginRequest(userData.username(), userData.password());
        // call the login method of the UserService
        LoginResponse loginResponse = userService.login(loginRequest);
        // return the LoginResponse object as a JSON string
        return new Gson().toJson(loginResponse);
    }
}
