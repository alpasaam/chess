package dataaccess;

import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {
    AuthDAO authDAO;

    @BeforeEach
    void setUp() throws ResponseException {
        authDAO = new SQLAuthDAO();
        authDAO.clear();
    }

    @Test
    void createAuth_Success() throws ResponseException {
        AuthData authData = new AuthData("username", "authToken");
        authDAO.createAuth(authData);
        AuthData retrievedAuth = authDAO.getAuth("authToken");
        assertNotNull(retrievedAuth);
        assertEquals("authToken", retrievedAuth.authToken());
        assertEquals("username", retrievedAuth.username());
    }

    @Test
    void createAuth_DuplicateAuthToken() throws ResponseException {
        AuthData authData = new AuthData("username", "authToken");
        authDAO.createAuth(authData);
        ResponseException exception = assertThrows(ResponseException.class, () -> authDAO.createAuth(authData));
        assertEquals(500, exception.statusCode());
    }

    @Test
    void getAuth_NonExistent() throws ResponseException {
        AuthData authData = authDAO.getAuth("nonexistent");
        assertNull(authData);
    }

    @Test
    void clear_Success() throws ResponseException {
        AuthData authData = new AuthData("username", "authToken");
        authDAO.createAuth(authData);
        authDAO.clear();
        AuthData retrievedAuth = authDAO.getAuth("authToken");
        assertNull(retrievedAuth);
    }
}