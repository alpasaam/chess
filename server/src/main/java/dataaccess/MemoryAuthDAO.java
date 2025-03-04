package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{

    private final HashMap<String, AuthData> auths = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) {
        auths.putIfAbsent(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return auths.getOrDefault(authToken, null);
    }

    @Override
    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    @Override
    public void clear() {
        auths.clear();
    }
}
