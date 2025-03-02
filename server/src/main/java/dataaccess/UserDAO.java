package dataaccess;

public interface UserDAO {
    public void createUser(String username, String password, String email);
    public void getUser(String username);
}
