package dataaccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData userData);
    void getUser(String username);
    void clear();
}
