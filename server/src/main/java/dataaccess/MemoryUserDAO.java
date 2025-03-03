package dataaccess;

import model.UserData;

import java.util.HashMap;


public class MemoryUserDAO implements UserDAO{
    private final HashMap<String, UserData> users = new HashMap<String, UserData>();


    @Override
    public void createUser(String username, String password, String email) {
        users.putIfAbsent(username, new UserData(username, password, email));
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
