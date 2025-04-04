package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, registerRequest, RegisterResponse.class, null);
    }

    public LoginResponse login(LoginRequest loginRequest) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, loginRequest, LoginResponse.class, null);
    }

    public void logout(String authorization) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authorization);
    }

    public Collection<GameData> listGames(String authorization) throws ResponseException {
        var path = "/game";
        record ListGameResponse(Collection<GameData> games) {
        }
        var response = this.makeRequest("GET", path, null, ListGameResponse.class, authorization);
        return response.games();

    }

    public NewGameResponse createGame(NewGameRequest newGameRequest) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, newGameRequest, NewGameResponse.class, newGameRequest.authToken());
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, joinGameRequest, null, joinGameRequest.authToken());
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authorization) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authorization != null) {
                http.setRequestProperty("Authorization", authorization);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
