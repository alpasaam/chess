package service;

import dataaccess.*;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;
    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();

    @BeforeEach
    void setUp() {
        userService = new UserService(userDAO, authDAO);
    }

    @AfterEach
    void tearDown() throws ResponseException {
        userDAO.clear();
        authDAO.clear();
    }

    // Positive test case for generateToken
    @Test
    void generateTokenPositive() {
        String token = UserService.generateToken();
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void loginPositive() throws ResponseException {
        // Register the user first
        RegisterRequest registerRequest = new RegisterRequest("testUser", "testPass", "test@example.com");
        userService.register(registerRequest);

        // Attempt to log in with the correct password
        LoginRequest loginRequest = new LoginRequest("testUser", "testPass");
        LoginResponse response = userService.login(loginRequest);

        assertNotNull(response);
        assertEquals("testUser", response.username());
        assertNotNull(response.authToken());
    }

    // Negative test case for login (401 error)
    @Test
    void loginNegative() throws ResponseException {
        String username = "testUser";
        String password = "testPass";
        String wrongPassword = "wrongPass";

        // Register the user first
        RegisterRequest registerRequest = new RegisterRequest(username, password, "test@example.com");
        userService.register(registerRequest);

        // Attempt to log in with the wrong password
        LoginRequest loginRequest = new LoginRequest(username, wrongPassword);
        assertThrows(IllegalArgumentException.class, () -> userService.login(loginRequest));
    }

    // Positive test case for register
    @Test
    void registerPositive() throws ResponseException {
        String username = "newUser";
        String password = "newPass";
        String email = "new@example.com";
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);

        RegisterResponse response = userService.register(registerRequest);

        assertNotNull(response);
        assertEquals(username, response.username());
        assertNotNull(response.authToken());
    }

    // Negative test case for register (401 error)
    @Test
    void registerNegative() throws ResponseException {
        String username = "existingUser";
        String password = "newPass";
        String email = "new@example.com";
        userDAO.createUser(new UserData(username, "existingPass", "existing@example.com"));
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);

        assertThrows(ResponseException.class, () -> userService.register(registerRequest));
    }

    // Positive test case for logout
    @Test
    void logoutPositive() throws ResponseException {
        String authToken = "validAuthToken";
        authDAO.createAuth(new AuthData("username", authToken));

        userService.logout(authToken);

        assertNull(authDAO.getAuth(authToken));
    }

    // Negative test case for logout (401 error)
    @Test
    void logoutNegative() {
        String invalidAuthToken = "invalidAuthToken";

        assertThrows(ResponseException.class, () -> userService.logout(invalidAuthToken));
    }

    // Positive test case for clear
    @Test
    void clearPositive() throws ResponseException {
        userDAO.createUser(new UserData("username", "password", "email@example.com"));
        authDAO.createAuth(new AuthData("username", "authToken"));

        userService.clear();

        assertNull(userDAO.getUser("username"));
        assertNull(authDAO.getAuth("authToken"));
    }
}