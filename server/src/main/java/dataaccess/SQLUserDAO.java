package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO() throws ResponseException {
        try {
            String[] createStatements = {
                    """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            };
            BaseSQLDAO.configureDatabase(createStatements);
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    @Override
    public void createUser(UserData userData) throws ResponseException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                var hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
                ps.setString(1, userData.username());
                ps.setString(2, hashedPassword);
                ps.setString(3, userData.email());
                ps.executeUpdate();
            }
    } catch (SQLException e) {
            throw new ResponseException(500, String.format("Unable to create user: %s", e.getMessage()));
        }
    }

    @Override
    public UserData getUser(String username) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM users WHERE username = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()){
                    if (rs.next()) {
                        var rsUsername = rs.getString("username");
                        var rsPassword = rs.getString("password");
                        var rsEmail = rs.getString("email");
                        return new UserData(rsUsername,rsPassword,rsEmail);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void clear() throws ResponseException {
        var statement = "DELETE FROM users";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("Unable to clear data: %s", e.getMessage()));
        }


    }


}
