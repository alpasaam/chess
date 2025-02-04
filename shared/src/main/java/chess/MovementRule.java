package chess;

import java.util.Collection;

public interface MovementRule {
    Collection<ChessMove> validMoves(ChessPosition startPosition);
}
