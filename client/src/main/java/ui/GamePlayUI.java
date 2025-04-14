package ui;

import chess.ChessGame;
import exception.ResponseException;

import java.util.Scanner;

import static java.lang.System.out;

public class GamePlayUI {
    private final Scanner scanner = new Scanner(System.in);

    private final Repl repl;

    public GamePlayUI(Repl repl) {
        this.repl = repl;
    }

    public void run() {
        out.println("Welcome to the GamePlay UI! Press enter to get help.");
        drawChessBoard();

        String result = "";
        while (!result.equals("leave")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                out.print(result);
            } catch (Throwable e) {
                out.print(e.getMessage());
            }
        }
        out.println();
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
        out.print("\n>>> ");
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

    private void drawChessBoard(){
        GameBoardUI.drawChessBoard(out, repl.getColor().equals("WHITE"), repl.getGame().game().getBoard());

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
        out.println("Making move from " + from + " to " + to + "...");
        // Example: repl.client.makeMove(repl.authToken, repl.gameID, new ChessMove(from, to));
        return "Move made successfully.\n";
    }

    private String resignGame() throws ResponseException {
        repl.getWebSocketFacade().resign(repl.getAuthToken(), repl.getGame().gameID());
        return "You have resigned from the game.\n";
    }

    private String highlightMoves(String[] tokens) throws ResponseException {

    }
}