package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public void createAuth(AuthData authData);
    public void getAuth(String authToken) throws DataAccessException;
}
