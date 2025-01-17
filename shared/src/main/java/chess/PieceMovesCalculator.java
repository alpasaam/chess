package chess;

import java.util.Collection;

public interface PieceMovesCalculator extends KingMovesCalculator, QueenMovesCalculator, BishopMovesCalculator, KnightMovesCalculator, RookMovesCalculator, PawnMovesCalculator{
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}
