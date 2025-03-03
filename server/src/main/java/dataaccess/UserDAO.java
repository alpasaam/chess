package dataaccess;

import model.UserData;

public interface UserDAO {
    public void createUser(UserData userData);
    public void getUser(String username);
    public void clear();
}
