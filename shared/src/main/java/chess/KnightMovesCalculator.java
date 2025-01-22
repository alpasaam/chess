package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int[] dx = {1, 2, 2, 1, -1, -2, -2, -1};
        int[] dy = {2, 1, -1, -2, -2, -1, 1, 2};
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        for (int i = 0; i < 8; i++) {
            int x = myPosition.getRow() + dx[i];
            int y = myPosition.getColumn() + dy[i];
            ChessPosition newPosition = new ChessPosition(x, y);
            if (newPosition.isNotValid()) {
                continue;
            }
            ChessPiece piece = board.getPiece(newPosition);
            if (piece == null || piece.getTeamColor() != myColor) {
                moves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        return moves;
    }
}