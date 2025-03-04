package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import exception.ResponseException;
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
        authDAO.createAuth(new AuthData(username, authToken));
        return new LoginResponse(username, authToken);
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws ResponseException {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();
        if (username == null || password == null || email == null) {
            throw new ResponseException(400, "Error: bad request");
        }
        if (userDAO.getUser(username) != null){
            throw new ResponseException(403, "Error: already taken");
        }
        String authToken = generateToken();
        userDAO.createUser(new UserData(username, password, email));
        authDAO.createAuth(new AuthData(username, authToken));
        return new RegisterResponse(username, authToken);
    }

    public void logout(String authorization) throws ResponseException {
        authDAO.getAuth(authorization);
        authDAO.deleteAuth(authorization);
    }
}
