import chess.*;
import ui.ChessClient;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        //start code here
        /*
        things to do for phase 5
        1. Draw Menus & Handle Input
        2. Draw Chess Board
        3. Invoke Server API Endpoint
        4. Write Tests
        */

        var serverUrl = "http://localhost:8080";
        if (args.length == 1){
            serverUrl = args[0];
        }

        new ChessClient(serverUrl).start();
    }
}