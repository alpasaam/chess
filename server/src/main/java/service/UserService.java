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

    public LoginResponse login(LoginRequest loginRequest) throws ResponseException {
        String username = loginRequest.username();
        String password = loginRequest.password();
        String authToken = generateToken();
        if (username == null || password == null) {
            throw new ResponseException(400, "Error: bad request");
        }
        if (userDAO.getUser(username) == null || !userDAO.getUser(username).password().equals(password)) {
            throw new ResponseException(401, "Error: unauthorized");
        }
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
        if (authDAO.getAuth(authorization) == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        authDAO.deleteAuth(authorization);
    }

    public void clear() {
        userDAO.clear();
        authDAO.clear();
    }
}
