import chess.*;
import ui.ChessClient;
import ui.Repl;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        //start code here

        var serverUrl = "http://localhost:8080";
        if (args.length == 1){
            serverUrl = args[0];
        }

        // new ChessClient(serverUrl).start();
        new Repl(serverUrl).run();
    }
}