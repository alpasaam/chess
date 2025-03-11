package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    void tearDown() {
    }

    @Test
    void createUser_Success() throws ResponseException {
        UserData user = new UserData("username", "password", "email@example.com");
        userDAO.createUser(user);
        UserData retrievedUser = userDAO.getUser("username");
        assertNotNull(retrievedUser);
        assertEquals("username", retrievedUser.username());
        assertTrue(BCrypt.checkpw("password", retrievedUser.password()));
        assertEquals("email@example.com", retrievedUser.email());
    }

    @Test
    void createUser_DuplicateUsername() throws ResponseException {
        SQLUserDAO userDAO = new SQLUserDAO();
        UserData user = new UserData("username", "password", "email@example.com");
        userDAO.createUser(user);
        ResponseException exception = assertThrows(ResponseException.class, () -> {
            userDAO.createUser(user);
        });
        assertEquals(500, exception.statusCode());
    }

    @Test
    void getUser_NonExistent() throws ResponseException {
        SQLUserDAO userDAO = new SQLUserDAO();
        UserData user = userDAO.getUser("nonexistent");
        assertNull(user);
    }

    @Test
    void clear_Success() throws ResponseException {
        SQLUserDAO userDAO = new SQLUserDAO();
        UserData user = new UserData("username", "password", "email@example.com");
        userDAO.createUser(user);
        userDAO.clear();
        UserData retrievedUser = userDAO.getUser("username");
        assertNull(retrievedUser);
    }

}