package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{

    private final HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) throws ResponseException {
        auths.putIfAbsent(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws ResponseException {
        return auths.getOrDefault(authToken, null);
    }

    @Override
    public void deleteAuth(String authToken) throws ResponseException {
        auths.remove(authToken);
    }

    @Override
    public void clear() throws ResponseException {
        auths.clear();
    }
}
