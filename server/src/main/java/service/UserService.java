package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.*;

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

    public RegisterResponse register(RegisterRequest registerRequest) {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        String authToken = generateToken();
        userDAO.createUser(new UserData(username, password, email));
        authDAO.createAuth(new AuthData(authToken, username));
        return new RegisterResponse(username, authToken);
    }

    public void clear() {
        userDAO.clear();
        authDAO.clear();
    }
}
