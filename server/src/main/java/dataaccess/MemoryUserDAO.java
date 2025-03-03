package dataaccess;

import model.UserData;

import java.util.HashMap;


public class MemoryUserDAO implements UserDAO{
    private final HashMap<String, UserData> users = new HashMap<>();


    @Override
    public void createUser(UserData userData) {
        users.putIfAbsent(userData.username(), userData);
    }

    @Override
    public void getUser(String username) {
        users.getOrDefault(username, null);
    }

    @Override
    public void clear(){
        users.clear();
    }
}
