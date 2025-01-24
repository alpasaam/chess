package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int[] directions = {-1, 0, 1};
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                BishopMovesCalculator.implementBishopQueenDirections(board, myPosition, moves, myColor, dx, dy);
            }
        }
        return moves;
    }
}