package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int[] directions = {-1,1};
        for (int dx : directions) {
            int x = myPosition.getRow();
            int y = myPosition.getColumn();
            while (true) {
                x += dx;
                ChessPosition newPosition = new ChessPosition(x, y);
                if (BishopMovesCalculator.checkIfBishopRookAbleToMoveTo(board, myPosition, moves, myColor, newPosition)) {
                    break;
                }
            }
        }

        for (int dy : directions) {
            int x = myPosition.getRow();
            int y = myPosition.getColumn();
            while (true) {
                y += dy;
                ChessPosition newPosition = new ChessPosition(x, y);
                if (BishopMovesCalculator.checkIfBishopRookAbleToMoveTo(board, myPosition, moves, myColor, newPosition)) {
                    break;
                }
            }
        }


        return moves;
    }
}