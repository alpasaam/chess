package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.LoginRequest;
import model.LoginResponse;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public LoginResponse login(LoginRequest loginRequest){
        String username = loginRequest.username();
        String password = loginRequest.password();
        String authToken = generateToken();
        // check if user exists

        // check if password is correct

        // create auth token
        authDAO.createAuth(new AuthData(authToken, username));
        return new LoginResponse(username, authToken);
    }

}
