package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws ResponseException {
        try {
            configureDatabase();
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    @Override
    public GameData createGame(GameData gameData) throws ResponseException {
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                if (gameData.gameName() == null) {
                    throw new ResponseException(400, "Error: bad request");
                }
                ps.setString(1, gameData.whiteUsername());
                ps.setString(2, gameData.blackUsername());
                ps.setString(3, gameData.gameName());
                ps.setString(4, new Gson().toJson(new ChessGame(), ChessGame.class));
                ps.executeUpdate();

                try (var rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return new GameData(
                                rs.getInt(1),
                                gameData.whiteUsername(),
                                gameData.blackUsername(),
                                gameData.gameName(),
                                gameData.game()
                        );
                    }
                }
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("Unable to create game: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws ResponseException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
        Collection<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ChessGame chessGame = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        games.add(new GameData(
                                rs.getInt("gameID"),
                                rs.getString("whiteUsername"),
                                rs.getString("blackUsername"),
                                rs.getString("gameName"),
                                chessGame
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            throw new ResponseException(600, String.format("Unable to list games: %s", e.getMessage()));
        }
        return games;
    }

    @Override
    public GameData getGame(int id) throws ResponseException {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new GameData(
                                rs.getInt("gameID"),
                                rs.getString("whiteUsername"),
                                rs.getString("blackUsername"),
                                rs.getString("gameName"),
                                new Gson().fromJson(rs.getString("game"), ChessGame.class)
                        );
                    }
                }
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("Unable to get game: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void updateGame(GameData gameData) throws ResponseException {
        var statement = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, gameData.whiteUsername());
                ps.setString(2, gameData.blackUsername());
                ps.setString(3, gameData.gameName());
                ps.setString(4, new Gson().toJson(gameData));
                ps.setInt(5, gameData.gameID());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("Unable to update game: %s", e.getMessage()));
        }
    }

    @Override
    public void clear() throws ResponseException {
        var statement = "DELETE FROM games";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("Unable to clear data: %s", e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
              gameID int NOT NULL AUTO_INCREMENT,
              whiteUsername VARCHAR(256),
              blackUsername VARCHAR(256),
              gameName VARCHAR(256) NOT NULL,
              game JSON NOT NULL,
              PRIMARY KEY (gameID)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws ResponseException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}