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
                int x = myPosition.getRow();
                int y = myPosition.getColumn();
                while (true) {
                    x += dx;
                    y += dy;
                    ChessPosition newPosition = new ChessPosition(x, y);
                    if (!newPosition.isValid()) {
                        break;
                    }
                    ChessPiece piece = board.getPiece(newPosition);
                    if (piece == null ) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    } else {
                        if (piece.getTeamColor() != myColor) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                        break;
                    }
                }
            }
        }
        return moves;
    }
}