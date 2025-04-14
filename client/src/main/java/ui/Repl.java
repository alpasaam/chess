package ui;

import com.google.gson.Gson;
import ui.websocket.NotificationHandler;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println("♟️ Welcome to the Chess Client.");

        PreloginUI preloginUI = new PreloginUI(client);
        preloginUI.run();

    }

    public void notify(ServerMessage serverMessage) {
        Gson gson = new Gson();

        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> {
                LoadGameMessage loadGameMessage = gson.fromJson(gson.toJson(serverMessage), LoadGameMessage.class);
                System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + "Game loaded: " + loadGameMessage.getGame());
            }
            case ERROR -> {
                ErrorMessage errorMessage = gson.fromJson(gson.toJson(serverMessage), ErrorMessage.class);
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Error: " + errorMessage.getErrorMessage());
            }
            case NOTIFICATION -> {
                NotificationMessage notificationMessage = gson.fromJson(gson.toJson(serverMessage), NotificationMessage.class);
                System.out.println(EscapeSequences.SET_TEXT_COLOR_YELLOW + "Notification: " + notificationMessage.getMessage());
            }
            default -> System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Unknown message type received.");
        }
    }
}