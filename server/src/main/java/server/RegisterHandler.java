package server;


import com.google.gson.Gson;
import exception.ResponseException;
import model.RegisterRequest;
import model.RegisterResponse;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    final UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public Object register(Request req, Response res) throws ResponseException {
        // parse the request body into a UserData object
        UserData userData = new Gson().fromJson(req.body(), UserData.class);
        // create a RegisterRequest object
        RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());
        // call the register method of the UserService
        RegisterResponse registerResponse = userService.register(registerRequest);
        // return the RegisterResponse object as a JSON string
        return new Gson().toJson(registerResponse);
    }
}
