package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int[] directions = {-1, 1};

        for (int dx : directions) {
            PieceMovesCalculator.directionalMoves(board, myPosition, moves, myColor, dx, 0);
        }

        for (int dy : directions) {
            PieceMovesCalculator.directionalMoves(board, myPosition, moves, myColor, 0, dy);
        }

        return moves;
    }
}