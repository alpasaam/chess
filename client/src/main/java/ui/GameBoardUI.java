// create the board in this class
//make a main method to see what the board looks like when coding it
// follow tic tak toe
// have last method where you draw white or black square check what piece it needs to print out.

package ui;

// print the black perspective when joining as black


import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessGame.TeamColor;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class GameBoardUI {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        ChessBoard chessBoard = new ChessBoard(); // Instantiate a new ChessBoard
        chessBoard.resetBoard(); // Set the board to the default starting position

        out.print(ERASE_SCREEN);

        drawChessBoard(out, false, chessBoard, null); // Draw board for white player

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
            for (int i = columnLabels.length - 1; i >= 0; i--) {
                out.print(columnLabels[i]);
            }
        }
        out.print("   ");
        setDefault(out);
        out.println();
    }

    public static void drawChessBoard(PrintStream out, boolean isWhitePlayer, ChessBoard chessBoard, Collection<ChessPosition> positionsToHighlight) {
        setDefault(out);
        drawHeader(out, isWhitePlayer);
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            drawRowOfSquares(out, boardRow, isWhitePlayer, chessBoard, positionsToHighlight);
        }
        drawHeader(out, isWhitePlayer);
        setDefault(out);
    }

    private static void drawRowOfSquares(PrintStream out, int boardRow, boolean isWhitePlayer,
                                         ChessBoard chessBoard, Collection<ChessPosition> positionsToHighlight) {
        setGrey(out);
        out.print(" " + (!isWhitePlayer ? boardRow + 1 : BOARD_SIZE_IN_SQUARES - boardRow) + " "); // Print row number
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            int row = isWhitePlayer ? BOARD_SIZE_IN_SQUARES - boardRow - 1 : boardRow;
            int col = isWhitePlayer ? boardCol : BOARD_SIZE_IN_SQUARES - boardCol - 1;

            ChessPosition position = new ChessPosition(row + 1, col + 1);

            // Check if the position is in the highlighted positions
            if (positionsToHighlight != null && positionsToHighlight.contains(position)) {
                setYellow(out); // Highlight the square
            } else if ((boardRow + boardCol) % 2 == 0) {
                setWhite(out);
            } else {
                setBlack(out);
            }

            printPiece(out, chessBoard.getPiece(position)); // Use chessBoard to get piece
            setBlack(out);
        }
        setGrey(out);
        out.print(" " + (!isWhitePlayer ? boardRow + 1 : BOARD_SIZE_IN_SQUARES - boardRow) + " "); // Print row number at the end
        setDefault(out);
        out.println();
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

    private static void setYellow(PrintStream out) {
        out.print(SET_BG_COLOR_YELLOW);
    }

    private static void setDefault (PrintStream out){
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
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

    public static void highlight(PrintStream out, ChessBoard chessBoard, ChessPiece piece, ChessPosition position, boolean isWhitePlayer) {
        if (piece == null) {
            out.println("No piece at the given position.");
            return;
        }

        // Get legal moves for the piece
        Collection<ChessPosition> legalMoves = piece.pieceMoves(chessBoard, position).stream()
                .map(ChessMove::getEndPosition)
                .toList();

        if (legalMoves.isEmpty()) {
            out.println("No legal moves available for the selected piece.");
            return;
        }

        // Redraw the chessboard with highlights
        drawChessBoard(out, isWhitePlayer, chessBoard, legalMoves);
    }

}