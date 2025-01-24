package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int[] directions = {-1, 1};
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();

        for (int dx : directions) {
            for (int dy : directions) {
                implementBishopQueenDirections(board, myPosition, moves, myColor, dx, dy);
            }
        }
        return moves;
    }

    static void implementBishopQueenDirections(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, ChessGame.TeamColor myColor, int dx, int dy) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn();
        while (true) {
            x += dx;
            y += dy;
            ChessPosition newPosition = new ChessPosition(x, y);
            if (checkIfBishopRookAbleToMoveTo(board, myPosition, moves, myColor, newPosition)) {
                break;
            }
        }
    }

    static boolean checkIfBishopRookAbleToMoveTo(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, ChessGame.TeamColor myColor, ChessPosition newPosition) {
        if (!newPosition.isValid()) {
            return true;
        }
        ChessPiece piece = board.getPiece(newPosition);
        if (piece == null ) {
            moves.add(new ChessMove(myPosition, newPosition, null));
        } else {
            if (piece.getTeamColor() != myColor) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
            return true;
        }
        return false;
    }
}