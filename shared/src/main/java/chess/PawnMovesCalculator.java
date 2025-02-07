package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int direction = (myColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (myColor == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int promotionRow = (myColor == ChessGame.TeamColor.WHITE) ? 8 : 1;
        int[] captureDirection = {-1, 1};

        ChessPosition forwardPosition = new ChessPosition(myPosition.getRow() + direction, myPosition.getColumn());
        if (forwardPosition.isValid() && board.getPiece(forwardPosition) == null) {
            if (forwardPosition.getRow() == promotionRow) {
                moves.add(new ChessMove(myPosition, forwardPosition, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(myPosition, forwardPosition, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPosition, forwardPosition, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(myPosition, forwardPosition, ChessPiece.PieceType.KNIGHT));
            } else {
                moves.add(new ChessMove(myPosition, forwardPosition, null));
            }
            ChessPosition doubleForwardPosition = new ChessPosition(myPosition.getRow() + 2 * direction, myPosition.getColumn());
            if (myPosition.getRow() == startRow) {
                if (board.getPiece(doubleForwardPosition) == null) {
                    moves.add(new ChessMove(myPosition, doubleForwardPosition, null));
                }
            }
        }


        for (int dx: captureDirection){
            ChessPosition capturePosition = new ChessPosition(myPosition.getRow() +direction, myPosition.getColumn()+dx);
            if (!capturePosition.isValid()){
                continue;
            }
            ChessPiece piece = board.getPiece(capturePosition);
            if (piece != null && piece.getTeamColor() !=myColor){
                if (capturePosition.getRow() == promotionRow) {
                    moves.add(new ChessMove(myPosition, capturePosition, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, capturePosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, capturePosition, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, capturePosition, ChessPiece.PieceType.KNIGHT));
                } else {
                    moves.add(new ChessMove(myPosition, capturePosition, null));
                }
            }
        }




        return moves;
    }
}
