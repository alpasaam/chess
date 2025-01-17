package chess;

import java.util.Collection;

public interface KingMovesCalculator implements PieceMovesCalculator {
    default Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}
