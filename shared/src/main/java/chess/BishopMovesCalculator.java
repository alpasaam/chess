package chess;

import java.util.Collection;

public interface BishopMovesCalculator implements PieceMovesCalculator {
    default Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }
}