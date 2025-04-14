package ui;

// display existing users in game when listing, show what color they represent

import exception.ResponseException;
import model.GameData;

import java.util.*;

public class PostloginUI {
    private final Scanner scanner = new Scanner(System.in);
    private final ChessClient client;
    private final String authToken;
    private Collection<GameData> games;

    public PostloginUI(ChessClient client, String authToken) {
        this.client = client;
        this.authToken = authToken;
    }

    public void run() {
        System.out.println("Welcome to the Postlogin UI! Press enter to get help.");

        String result = "";
        while (!result.equals("logout")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }

    private String eval(String input) throws ResponseException {
        var result = "Invalid input \n";
        var tokens = input.toLowerCase().split(" ");
        if (tokens.length > 0) {
            var cmd = tokens[0];
            result = switch (cmd) {
                case "help" -> help();
                case "logout" -> logout();
                case "create" -> createGame();
                case "list" -> listGames();
                case "play" -> playGame(tokens);
                case "observe" -> observeGame(tokens);
                default -> result + help();
            };
        }
        return result;
    }

    private void printPrompt() {
        System.out.print("\n>>> ");
    }

    private String help() {
        return """
                Available commands:
                - help: Displays this help message.
                - logout: Logs out the user.
                - create: Allows the user to create a new game.
                - list: Lists all the games that currently exist on the server.
                - play <game number> <color>: Allows the user to join a game.
                - observe <game number>: Allows the user to observe a game.
                """;
    }

    private String logout() throws ResponseException {
        try {
            client.logout(authToken);
            System.out.println("Logout successful!");
            PreloginUI preloginUI = new PreloginUI(client);
            preloginUI.run();
            return "logout";
        } catch (Exception e) {
            throw new ResponseException(400, "Logout failed: Unable to log out.");
        }
    }

    private String createGame() throws ResponseException {
        try {
            System.out.println("Please enter a name for the new game:");
            String gameName = scanner.nextLine();
            client.createGame(gameName, authToken);
            return "Game created successfully!";
        } catch (Exception e) {
            throw new ResponseException(400, "Game creation failed: Unable to create a new game.");
        }
    }

    private String listGames() throws ResponseException {
        try {
            games = client.listGames(authToken);
            if (games != null && !games.isEmpty()) {
                StringBuilder result = new StringBuilder("Games:\n");
                int index = 1;
                for (GameData game : games) {
                    result.append(index++)
                            .append(". ")
                            .append(game.gameName())
                            .append(" - Players: ")
                            .append("White: ")
                            .append(game.whiteUsername())
                            .append(", ")
                            .append("Black: ")
                            .append(game.blackUsername())
                            .append("\n");
                }
                return result.toString();
            } else {
                return "No games available.";
            }
        } catch (Exception e) {
            throw new ResponseException(400, "Failed to list games: Unable to retrieve game list.");
        }
    }

    private String playGame(String[] tokens) throws ResponseException {
        try {
            if (tokens.length < 3) {
                throw new ResponseException(400, "Usage: play <game number> <color>");
            }

            int gameNumber = Integer.parseInt(tokens[1]);
            String color = tokens[2].toUpperCase();

            if (gameNumber < 1 || gameNumber > games.size()) {
                throw new ResponseException(400, "Invalid game number: The specified game number is out of range.");
            }

            List<GameData> gameList = new ArrayList<>(games);
            GameData game = gameList.get(gameNumber - 1);
            client.joinGame(authToken, color, game.gameID());

            GameBoardUI.drawChessBoard(System.out, color.equals("WHITE"), game.game().getBoard());

            return "Joined game successfully!";
        } catch (NumberFormatException e) {
            throw new ResponseException(400, "Invalid game number: The game number must be an integer.");
        } catch (Exception e) {
            throw new ResponseException(400, "Failed to join game: Unable to join the game.");
        }
    }

    private String observeGame(String[] tokens) throws ResponseException {
        try {
            if (tokens.length < 2) {
                throw new ResponseException(400, "Usage: observe <game number>");
            }

            int gameNumber = Integer.parseInt(tokens[1]);

            if (gameNumber < 1 || gameNumber > games.size()) {
                throw new ResponseException(400, "Invalid game number: The specified game number is out of range.");
            }

            List<GameData> gameList = new ArrayList<>(games);
            GameData game = gameList.get(gameNumber - 1);
            client.observeGame(game.gameID(), authToken);
            return "Observing game successfully!";
        } catch (NumberFormatException e) {
            throw new ResponseException(400, "Invalid game number: The game number must be an integer.");
        } catch (Exception e) {
            throw new ResponseException(400, "Failed to observe game: Unable to observe the game.");
        }
    }
}