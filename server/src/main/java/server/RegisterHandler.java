package server;


import service.UserService;

public class RegisterHandler {

    UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public String register(Request req, Response resp) throws Exception {
        return userService.register(registerRequest);
    }

}
