package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int[] dx = {-2,-2,-1,-1,1,1,2,2};
        int[] dy = {-1,1,-2,2,-2,2,-1,1};

        for (int i = 0; i < 8; i++) {
            int x = myPosition.getRow() + dx[i];
            int y = myPosition.getColumn() + dy[i];
            PieceMovesCalculator.staticMoves(board, myPosition, moves, myColor, x, y);
        }


        return moves;
    }
}
