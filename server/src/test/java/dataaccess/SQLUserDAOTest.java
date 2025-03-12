package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {
    SQLUserDAO userDAO;

    @BeforeEach
    void setUp() throws ResponseException {
        userDAO = new SQLUserDAO();
        userDAO.clear();
    }


    @Test
    void createUserPositive() throws ResponseException {
        UserData user = new UserData("username", "password", "email@example.com");
        userDAO.createUser(user);
        UserData retrievedUser = userDAO.getUser("username");
        assertNotNull(retrievedUser);
        assertEquals("username", retrievedUser.username());
        assertTrue(BCrypt.checkpw("password", retrievedUser.password()));
        assertEquals("email@example.com", retrievedUser.email());
    }

    @Test
    void createUserNegative() throws ResponseException {
        SQLUserDAO userDAO = new SQLUserDAO();
        UserData user = new UserData(null, "password", "email@example.com");
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            userDAO.createUser(user);
        });
        assertEquals(500, exception.statusCode());
    }

    @Test
    void getUserPositive() throws ResponseException {
        UserData user = new UserData("username", "password", "test@example.com");
        userDAO.createUser(user);
        UserData retrievedUser = userDAO.getUser("username");
        assertNotNull(retrievedUser);
    }

    @Test
    void getUserNegative() throws ResponseException {
        SQLUserDAO userDAO = new SQLUserDAO();
        UserData user = userDAO.getUser("nonexistent");
        assertNull(user);
    }

    @Test
    void clearPositive() throws ResponseException {
        SQLUserDAO userDAO = new SQLUserDAO();
        UserData user = new UserData("username", "password", "email@example.com");
        userDAO.createUser(user);
        userDAO.clear();
        UserData retrievedUser = userDAO.getUser("username");
        assertNull(retrievedUser);
    }

}