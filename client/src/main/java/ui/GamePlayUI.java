package ui;

import chess.ChessMove;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import ui.websocket.WebSocketFacade;

import java.util.Scanner;

public class GamePlayUI {
    private final Scanner scanner = new Scanner(System.in);
    private final WebSocketFacade webSocketFacade;
    private final String authToken;
    private final GameData game;
    private final boolean isObserving;

    public GamePlayUI(WebSocketFacade webSocketFacade, String authToken, GameData game, boolean isObserving) {
        this.webSocketFacade = webSocketFacade;
        this.authToken = authToken;
        this.game = game;
        this.isObserving = isObserving;
    }

    public void run() {
        System.out.println("Welcome to the GamePlay UI! Press enter to get help.");
        drawChessBoard();

        String result = "";
        while (!result.equals("exit")) {
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

    private void drawChessBoard() {
        boolean isWhitePerspective = isObserving || game.isWhitePlayer();
        GameBoardUI.drawChessBoard(System.out, isWhitePerspective, game.game().getBoard());
    }

    private String leaveGame() throws ResponseException {
        try {
            webSocketFacade.leave(authToken, game.gameID());
            System.out.println("You have left the game.");
            PostloginUI postloginUI = new PostloginUI(new ChessClient(), authToken);
            postloginUI.run();
            return "exit";
        } catch (Exception e) {
            throw new ResponseException(400, "Failed to leave the game: " + e.getMessage());
        }
    }

    private String makeMove(String[] tokens) throws ResponseException {
        try {
            if (tokens.length < 3) {
                throw new ResponseException(400, "Usage: move <from> <to>");
            }

            String from = tokens[1];
            String to = tokens[2];

            ChessMove move = new ChessMove(from, to, null);
            webSocketFacade.makeMove(authToken, game.gameID(), move);
            return "Move command sent successfully!";
        } catch (Exception e) {
            throw new ResponseException(400, "Failed to send move command: " + e.getMessage());
        }
    }

    private String resignGame() throws ResponseException {
        System.out.println("Are you sure you want to resign? (yes/no)");
        String confirmation = scanner.nextLine().toLowerCase();
        if (confirmation.equals("yes")) {
            try {
                webSocketFacade.resign(authToken, game.gameID());
                return "You have resigned from the game.\n";
            } catch (Exception e) {
                throw new ResponseException(400, "Failed to resign: " + e.getMessage());
            }
        }
        return "Resignation canceled.\n";
    }

    private String highlightMoves(String[] tokens) throws ResponseException {
        try {
            if (tokens.length < 2) {
                throw new ResponseException(400, "Usage: highlight <square>");
            }

            String square = tokens[1];
            // Assuming GameBoardUI has a method to highlight moves
            GameBoardUI.highlightLegalMoves(System.out, square, game.getBoard());
            return "Highlighted legal moves for piece at " + square + ".\n";
        } catch (Exception e) {
            throw new ResponseException(400, "Failed to highlight moves: " + e.getMessage());
        }
    }
}