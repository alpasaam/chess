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
        // Use the message type to decide how to deserialize and process it
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME -> {
                // Deserialize to LoadGameMessage and print with the correct color
                LoadGameMessage loadGameMessage = new Gson().fromJson(new Gson().toJson(serverMessage), LoadGameMessage.class);
                Object gameObject = loadGameMessage.getGame();
                game = new Gson().fromJson(new Gson().toJson(gameObject), GameData.class);
                System.out.println();
                GameBoardUI.drawChessBoard(System.out, !getColor().equals("BLACK"), game.game().getBoard(), null);
                setGame(game);


            }
            case ERROR -> {
                // Deserialize to ErrorMessage and print with the correct color
                ErrorMessage errorMessage = new Gson().fromJson(new Gson().toJson(serverMessage), ErrorMessage.class);
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Error Message: " + errorMessage.getErrorMessage() + EscapeSequences.RESET_TEXT_COLOR);
            }
            case NOTIFICATION -> {
                // Deserialize to NotificationMessage and print with the correct color
                NotificationMessage notificationMessage = new Gson().fromJson(new Gson().toJson(serverMessage), NotificationMessage.class);
                System.out.println(EscapeSequences.SET_TEXT_COLOR_YELLOW + "Notification Message: " + notificationMessage.getMessage() + EscapeSequences.RESET_TEXT_COLOR);
            }
            default -> {
                // Fallback case for unknown message type
                System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Unknown Server Message Type: " + serverMessage + EscapeSequences.RESET_TEXT_COLOR);
            }
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
        return client.getWebSocketFacade();
    }

    public void setWebSocketFacade(WebSocketFacade webSocketFacade) {
        this.webSocketFacade = webSocketFacade;
        client.setWebSocketFacade(webSocketFacade);
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