package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
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
        GameBoardUI.drawChessBoard(out, repl.getColor().equals("WHITE"), repl.getGame().game().getBoard(), null);

    }

    private String leaveGame() throws ResponseException {
        repl.setState(State.SIGNEDIN);
        repl.getWebSocketFacade().leave(repl.getAuthToken(), repl.getGame().gameID());

        repl.setGame(null);
        repl.setColor(null);

        return "You have left the game.\n";
    }

    private String makeMove(String[] tokens) throws ResponseException {
        ChessPiece.PieceType promotionPiece = null;
        if (tokens.length < 2 || tokens.length > 3) {
            throw new ResponseException(400, "Usage: makeMove <fromTo> [promotionType]");
        }

        String fromTo = tokens[1].toLowerCase();
        if (fromTo.length() != 4) {
            throw new ResponseException(400, "Invalid fromTo format. Expected format: <from><to> (e.g., a1h8).");
        }

        String from = fromTo.substring(0, 2);
        String to = fromTo.substring(2, 4);

        ChessPosition fromPosition;
        ChessPosition toPosition;

        if (tokens.length == 3) {
            promotionPiece = ChessPiece.PieceType.valueOf(tokens[2].toLowerCase());
        }

        try {
            fromPosition = fromAlgebraic(from);
            toPosition = fromAlgebraic(to);
            repl.setWebSocketFacade(repl.getWebSocketFacade());
            repl.getClient().makeMove(repl.getAuthToken(), repl.getGame().gameID(), new ChessMove(fromPosition, toPosition, promotionPiece));
            //update the game
            repl.setGame(repl.getGame());


        } catch (IllegalArgumentException e) {
            throw new ResponseException(400, e.getMessage());
        }

        String promotionType = null;
        if (tokens.length == 3) {
            promotionType = tokens[2].toUpperCase();
            if (!isValidPromotionType(promotionType)) {
                throw new ResponseException(400, "Invalid promotion type. Valid types: QUEEN, ROOK, BISHOP, KNIGHT.");
            }
        }

        out.println("Making move from " + from + " to " + to + (promotionType != null ? " with promotion to " + promotionType : "") + "...");
        out.println("Game state updated successfully.");
        return "Move made successfully.\n";
    }

    private boolean isValidPromotionType(String promotionType) {
        return promotionType.equals("QUEEN") || promotionType.equals("ROOK") || promotionType.equals("BISHOP") || promotionType.equals("KNIGHT");
    }

    private String resignGame() throws ResponseException {
        repl.getWebSocketFacade().resign(repl.getAuthToken(), repl.getGame().gameID());
        return "You have resigned from the game.\n";
    }

    private String highlightMoves(String[] tokens) throws ResponseException {
        if (tokens.length < 2) {
            throw new ResponseException(400, "Usage: highlight <square>");
        }

        String square = tokens[1].toLowerCase();
        ChessPosition position = fromAlgebraic(square);

        if (!position.isValid()) {
            throw new ResponseException(400, "Invalid square: " + square);
        }

        ChessGame game = repl.getGame().game();
        ChessPiece piece = game.getBoard().getPiece(position);

        if (piece == null) {
            return "No piece at the given position.\n";
        }

        GameBoardUI.highlight(out, game.getBoard(), piece, position, repl.getColor().equals("WHITE"));

        return "Highlighted legal moves for the piece at " + square + ".\n";
    }

    public static ChessPosition fromAlgebraic(String algebraic) {
        if (algebraic == null || algebraic.length() != 2) {
            throw new IllegalArgumentException("Invalid algebraic notation: " + algebraic + "needs to be 2 characters");
        }

        char columnChar = algebraic.charAt(0);
        char rowChar = algebraic.charAt(1);

        int col = columnChar - 'a' + 1;
        int row = rowChar - '1' + 1;

        if (col < 1 || col > 8 || row < 1 || row > 8) {
            throw new IllegalArgumentException("Invalid algebraic notation: " + algebraic);
        }

        return new ChessPosition(row, col);
    }
}