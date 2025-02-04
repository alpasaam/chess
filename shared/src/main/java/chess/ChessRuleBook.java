package chess;

import java.util.Collection;

public interface ChessRuleBook {
    Collection<ChessMove> validMoves(ChessPosition startPosition);
    Boolean isBoardValid(ChessBoard board);
    Boolean isInCheck(ChessBoard board, ChessGame.TeamColor teamColor);
    Boolean isInCheckMate(ChessBoard board, ChessGame.TeamColor teamColor);
    Boolean isInStaleMate(ChessBoard board, ChessGame.TeamColor teamColor);
}
