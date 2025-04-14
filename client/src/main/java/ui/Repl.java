package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class Repl implements NotificationHandler {
    private ChessClient client;
    private ServerFacade server;
    private String authToken;
    private WebSocketFacade webSocketFacade;
    private String serverUrl;
    private GameData game;
    private String color;
    private State state = State.SIGNEDOUT;


    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl, this);
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }


    public void run() {
        System.out.println("♟️ Welcome to the Chess Client.");

        setState(State.SIGNEDOUT);

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

    public GameData getGame() {
        return game;
    }

    public void setGame(GameData game) {
        this.game = game;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void newWebSocketFacade() throws ResponseException {
        this.webSocketFacade = new WebSocketFacade(serverUrl, this);
        client.setWebSocketFacade(webSocketFacade);
    }

    public WebSocketFacade getWebSocketFacade() {
        return webSocketFacade;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public ChessClient getClient() {
        return client;
    }

    public void setClient(ChessClient client) {
        this.client = client;
    }

    public ServerFacade getServer() {
        return server;
    }

    public void setServer(ServerFacade server) {
        this.server = server;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        switch (state) {
            case SIGNEDIN -> {
                PostloginUI postloginUI = new PostloginUI(this);
                postloginUI.run();
            }
            case SIGNEDOUT -> {
                PreloginUI preloginUI = new PreloginUI(this);
                preloginUI.run();
            }
            case INGAME -> {
                GamePlayUI gamePlayUI = new GamePlayUI(this);
                gamePlayUI.run();
            }
        }
    }

}