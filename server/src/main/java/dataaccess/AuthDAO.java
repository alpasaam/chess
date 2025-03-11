package dataaccess;

import exception.ResponseException;
import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData) throws ResponseException;
    AuthData getAuth(String authToken)throws ResponseException;
    void deleteAuth(String authToken) throws ResponseException;
    void clear() throws ResponseException;
}
