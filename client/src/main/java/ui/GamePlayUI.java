// create the board in this class
//make a main method to see what the board looks like when coding it
// follow tic tak toe
// have last method where you draw white or black square check what piece it needs to print out.

package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessGame.TeamColor;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class GamePlayUI {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        ChessBoard chessBoard = new ChessBoard(); // Instantiate a new ChessBoard
        chessBoard.resetBoard(); // Set the board to the default starting position

        out.print(ERASE_SCREEN);

        drawChessBoard(out, true, chessBoard); // Draw board for white player

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeader(PrintStream out, boolean isWhitePlayer) {
        setGrey(out);

        String[] columnLabels = {"\u2003A ", "\u2003B ", "\u2003C ", "\u2003D ", "\u2003E ", "\u2003F ",
                "\u2003G ", "\u2003H "};

        out.print("   "); // Print empty space before column labels
        // Draw column headers
        if (isWhitePlayer) {
            for (String columnLabel : columnLabels) {
                out.print(columnLabel);
            }
        } else {
            for (char col = 'H'; col >= 'A'; --col) {
                out.print(col);
            }
        }
        out.print("   ");
        setBlack(out);
        out.println();
    }

    private static void drawChessBoard(PrintStream out, boolean isWhitePlayer, ChessBoard chessBoard) {
        drawHeader(out, isWhitePlayer);
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            drawRowOfSquares(out, boardRow, isWhitePlayer, chessBoard);
        }
        drawHeader(out, isWhitePlayer);
    }

    private static void drawRowOfSquares(PrintStream out, int boardRow, boolean isWhitePlayer, ChessBoard chessBoard) {
        setGrey(out);
        out.print(" " + (isWhitePlayer ? boardRow + 1 : BOARD_SIZE_IN_SQUARES - boardRow) + " "); // Print row number
        for (int squareRow = SQUARE_SIZE_IN_PADDED_CHARS - 1; squareRow >= 0; --squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                int row = isWhitePlayer ? BOARD_SIZE_IN_SQUARES - boardRow - 1 : boardRow;
                int col = isWhitePlayer ? BOARD_SIZE_IN_SQUARES - boardCol - 1 : boardCol;

                // Determine the color based on the original board coordinates
                if ((boardRow + boardCol) % 2 == 0) {
                    setWhite(out);
                } else {
                    setBlack(out);
                }
                printPiece(out, chessBoard.getPiece(new ChessPosition(row + 1, col + 1))); // Use chessBoard to get piece
                setBlack(out);
            }
            setGrey(out);
            out.print(" " + (isWhitePlayer ? boardRow + 1 : BOARD_SIZE_IN_SQUARES - boardRow) + " "); // Print row number at the end
            setBlack(out);
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printPiece(PrintStream out, ChessPiece piece) {
        if (piece != null) {
            out.print(getPieceSymbol(piece));
            setWhite(out);
        } else {
            out.print(EscapeSequences.EMPTY);
        }
    }

    private static String getPieceSymbol(ChessPiece piece) {
        if (piece.getTeamColor() == TeamColor.WHITE) {
            return switch (piece.getPieceType()) {
                case KING -> WHITE_KING;
                case QUEEN -> WHITE_QUEEN;
                case BISHOP -> WHITE_BISHOP;
                case KNIGHT -> WHITE_KNIGHT;
                case ROOK -> WHITE_ROOK;
                case PAWN -> WHITE_PAWN;
            };
        } else {
            return switch (piece.getPieceType()) {
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case ROOK -> BLACK_ROOK;
                case PAWN -> BLACK_PAWN;
            };
        }
    }

    private static ChessPiece getChessPiece(int row, int col) {
        // This method should return the appropriate ChessPiece object based on the row and column.
        // For now, it returns null to indicate an empty square.
        return null;
    }
}