// create the board in this class
//make a main method to see what the board looks like when coding it
// follow tic tak toe
// have last method where you draw white or black square check what piece it needs to print out.

package ui;

import chess.ChessPiece;
import chess.ChessGame.TeamColor;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class GamePlayUI {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;

    // Padded characters.
    private static final String EMPTY = "   ";

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawChessBoard(out, true); // Draw board for white player

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeader(PrintStream out, boolean isWhitePlayer) {
        setGrey(out);

        // Draw column headers
        if (isWhitePlayer) {
            for (char col = 'A'; col <= 'H'; ++col) {
                out.print(" " + col + " ");
            }
        } else {
            for (char col = 'H'; col >= 'A'; --col) {
                out.print(" " + col + " ");
            }
        }
        out.println();
    }

    private static void drawChessBoard(PrintStream out, boolean isWhitePlayer) {
        drawHeader(out , isWhitePlayer);
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            drawRowOfSquares(out, boardRow, isWhitePlayer);
        }
        drawHeader(out, isWhitePlayer);
    }

    private static void drawRowOfSquares(PrintStream out, int boardRow, boolean isWhitePlayer) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                int row = isWhitePlayer ? boardRow : BOARD_SIZE_IN_SQUARES - boardRow - 1;
                int col = isWhitePlayer ? boardCol : BOARD_SIZE_IN_SQUARES - boardCol - 1;

                if ((row + col) % 2 == 0) {
                    setWhite(out);
                } else {
                    setBlack(out);
                }

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));
                    printPiece(out, getChessPiece(row, col));
                    out.print(EMPTY.repeat(suffixLength));
                } else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }

                setBlack(out);
            }
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printPiece(PrintStream out, ChessPiece piece) {
        if (piece != null) {
            out.print(SET_BG_COLOR_WHITE);
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(getPieceSymbol(piece));
            setWhite(out);
        } else {
            out.print(EMPTY);
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