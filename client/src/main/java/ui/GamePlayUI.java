package ui;

import exception.ResponseException;

import java.util.Scanner;

public class GamePlayUI {
    private final Scanner scanner = new Scanner(System.in);

    private final Repl repl;

    public GamePlayUI(Repl repl) {
        this.repl = repl;
    }

    public void run() {
        System.out.println("Welcome to the GamePlay UI! Press enter to get help.");
        drawChessBoard();

        String result = "";
        while (!result.equals("leave")) {
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
                case "redraw" -> {
                    drawChessBoard();
                    yield "Chess board redrawn.\n";
                }
                case "leave" -> leaveGame();
                case "move" -> makeMove(tokens);
                case "resign" -> resignGame();
                case "highlight" -> highlightMoves(tokens);
                default -> result + help();
            };
        }
        return result;
    }

    private void drawChessBoard() {
    }

    private void printPrompt() {
        System.out.print("\n>>> ");
    }

    private String help() {
        return """
                Available commands:
                - help: Displays this help message.
                - redraw: Redraws the chess board.
                - leave: Leaves the game and returns to the Post-Login UI.
                - move <from> <to>: Makes a move in the game.
                - resign: Resigns from the game.
                - highlight <square>: Highlights legal moves for a piece.
                """;
    }

    private String leaveGame() throws ResponseException {
        repl.setState(State.SIGNEDIN);
        repl.getWebSocketFacade().leave(repl.getAuthToken(), repl.getGame().gameID());

        repl.setGame(null);
        repl.setColor(null);

        return "You have left the game.\n";
    }

    private String makeMove(String[] tokens) throws ResponseException {
        // TODO: Implement logic to make a move in the game
        if (tokens.length < 3) {
            throw new ResponseException(400, "Usage: move <from> <to>");
        }
        String from = tokens[1];
        String to = tokens[2];
        System.out.println("Making move from " + from + " to " + to + "...");
        // Example: repl.client.makeMove(repl.authToken, repl.gameID, new ChessMove(from, to));
        return "Move made successfully.\n";
    }

    private String resignGame() throws ResponseException {
        // TODO: Implement logic to resign from the game
        System.out.println("Resigning from the game...");
        // Example: repl.client.resign(repl.authToken, repl.gameID);
        repl.setState(State.SIGNEDIN);
        return "You have resigned from the game.\n";
    }

    private String highlightMoves(String[] tokens) throws ResponseException {
        // TODO: Implement logic to highlight legal moves for a piece
        if (tokens.length < 2) {
            throw new ResponseException(400, "Usage: highlight <square>");
        }
        String square = tokens[1];
        System.out.println("Highlighting moves for square: " + square + "...");
        // Example: Fetch and display legal moves for the piece at the given square
        return "Highlighted moves for " + square + ".\n";
    }
}