package ui;

//register should log me in

import exception.ResponseException;
import model.LoginResponse;
import ui.websocket.NotificationHandler;

import java.util.Scanner;

public class PreloginUI {
    private final Scanner scanner = new Scanner(System.in);
    private final Repl repl;

    public PreloginUI(Repl repl) {
        this.repl = repl;
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
        System.exit(0);
    }

    private String eval(String input) throws ResponseException {
        var result = "Invalid input \n";
        var tokens = input.toLowerCase().split(" ");
        if (tokens.length > 0) {
            var cmd = tokens[0];
            result = switch (cmd) {
                case "help" -> help();
                case "quit" -> "quit";
                case "login" -> login();
                case "register" -> register();
                default -> result + help();
            };
        }
        return result;
    }

    private void printPrompt() {
        System.out.print("\n>>> ");
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

            LoginResponse response = repl.getClient().login(username, password);
            repl.setAuthToken(response.authToken());
            System.out.println("Login successful!");

            repl.setState(State.SIGNEDIN);

            return "login";
        } catch (Exception e) {
            throw new ResponseException(400, "Login failed: Unable to log in. Check username and password.");
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

            repl.getClient().register(username, password, email);
            System.out.println("Registration successful!");

            LoginResponse response = repl.getClient().login(username, password);
            repl.setAuthToken(response.authToken());
            System.out.println("Login successful!");

            repl.setState(State.SIGNEDIN);

            return "login";
        } catch (Exception e) {
            throw new ResponseException(400, "Registration failed: Enter valid username, password and email.");
        }
    }
}