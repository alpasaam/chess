package ui;

import exception.ResponseException;
import model.LoginResponse;

import java.util.Scanner;

public class PreloginUI {
    private final Scanner scanner = new Scanner(System.in);
    private final ChessClient client;
    private String authToken;

    public PreloginUI(ChessClient client) {
        this.client = client;
    }

    public void run() {
        System.out.println("Welcome to the Prelogin UI! Please login or register.");

        String result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n>>> ");
    }

    private String eval(String input) throws ResponseException {
        var result = "Invalid input";
        var tokens = input.toLowerCase().split(" ");
        if (tokens.length > 0) {
            var cmd = tokens[0];
            result = switch (cmd) {
                case "help" -> help();
                case "quit" -> "quit";
                case "login" -> login();
                case "register" -> register();
                default -> help();
            };
        }
        return result;
    }

    private String help() {
        return """
                Available commands:
                - help: Displays this help message.
                - quit: Quits the application.
                - login: Logs in the user.
                - register: Registers a new user.
                """;
    }

    private String login() throws ResponseException {
        try {
            System.out.println("Please enter your username:");
            String username = scanner.nextLine();
            System.out.println("Please enter your password:");
            String password = scanner.nextLine();

            LoginResponse response = client.login(username, password);
            authToken = response.authToken();
            System.out.println("Login successful!");

            PostloginUI postloginUI = new PostloginUI(client, authToken);
            postloginUI.run();
            return "login";
        } catch (Exception e) {
            throw new ResponseException(400, "Login failed: Unable to log in due to server error.");
        }
    }

    private String register() throws ResponseException {
        try {
            System.out.println("Please enter your username:");
            String username = scanner.nextLine();
            System.out.println("Please enter your password:");
            String password = scanner.nextLine();
            System.out.println("Please enter your email:");
            String email = scanner.nextLine();

            client.register(username, password, email);
            return "Registration successful! Please login.";
        } catch (Exception e) {
            throw new ResponseException(400, "Registration failed: Unable to register due to server error.");
        }
    }
}