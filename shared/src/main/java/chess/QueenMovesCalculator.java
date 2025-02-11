package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        int[] directions = {-1, 0, 1};

        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
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

        return moves;
    }
}
