package chess;

import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

    static void directionalMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> moves, ChessGame.TeamColor myColor, int dx, int dy) {
        int x = myPosition.getRow();
        int y = myPosition.getColumn();
        while (true){
            x += dx;
            y += dy;
            ChessPosition newPosition = new ChessPosition(x,y);
            if (!newPosition.isValid()){
                break;
            }
            ChessPiece piece = board.getPiece(newPosition);
            if (piece == null){
                moves.add(new ChessMove(myPosition,newPosition,null));
            } else {
                if (piece.getTeamColor() != myColor){
                    moves.add(new ChessMove(myPosition,newPosition,null));
                }
                break;
            }
        }
    }


}
