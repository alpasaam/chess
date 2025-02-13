# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

[![Sequence Diagram](Phase2.png)]
(https://sequencediagram.org/index.html#initialData=actor%20Client%0Aparticipant%20Server%0Aparticipant%20Handler%0Aparticipant%20Service%0Aparticipant%20DataAccess%0Adatabase%20db%0A%0Aentryspacing%200.9%0Agroup%20%23navy%20Registration%20%23white%0AClient%20-%3E%20Server%3A%20%5BPOST%5D%20%2Fuser%5Cn%7B%22username%22%3A%22%20%22%2C%20%22password%22%3A%22%20%22%2C%20%22email%22%3A%22%20%22%7D%0AServer%20-%3E%20Handler%3A%20%7B%22username%22%3A%22%20%22%2C%20%22password%22%3A%22%20%22%2C%20%22email%22%3A%22%20%22%7D%0AHandler%20-%3E%20Service%3A%20register(RegisterRequest)%0AService%20-%3E%20DataAccess%3A%20getUser(username)%0ADataAccess%20-%3E%20db%3AFind%20UserData%20by%20username%0ADataAccess%20--%3E%20Service%3A%20null%0AService%20-%3E%20DataAccess%3AcreateUser(userData)%0ADataAccess%20-%3E%20db%3AAdd%20UserData%0AService%20-%3E%20DataAccess%3AcreateAuth(authData)%0ADataAccess%20-%3E%20db%3AAdd%20AuthData%0AService%20--%3E%20Handler%3A%20RegisterResult%0AHandler%20--%3E%20Server%3A%20%7B%22username%22%20%3A%20%22%20%22%2C%20%22authToken%22%20%3A%20%22%20%22%7D%0AServer%20--%3E%20Client%3A%20200%5Cn%7B%22username%22%20%3A%20%22%20%22%2C%20%22authToken%22%20%3A%20%22%20%22%7D%0Aend%0A%0Agroup%20%23orange%20Login%20%23white%0AClient%20-%3E%20Server%3A%20%5BPOST%5D%20%2Fsession%5Cn%7Busername%2C%20password%7D%0AServer%20-%3E%20Handler%3A%20%7Busername%2C%20password%7D%0AHandler%20-%3E%20Service%3A%20login(LoginRequest)%0AService%20-%3E%20DataAccess%3A%20getAuth(authToken)%0ADataAccess%20-%3E%20db%3A%20Find%20authData%20by%20AuthToken%0ADataAccess%20--%3E%20Service%3A%20AuthData%0AService%20-%3E%20DataAccess%3A%20createAuth(authData)%0ADataAccess%20-%3E%20db%3A%20Add%20AuthData%0AService%20--%3E%20Handler%3A%20LoginResult%0AHandler%20--%3E%20Server%3A%7B%22username%22%20%3A%20%22%20%22%2C%20%22authToken%22%20%3A%20%22%20%22%7D%0AServer%20--%3E%20Client%3A%20200%5Cn%7B%22username%22%20%3A%20%22%20%22%2C%20%22authToken%22%20%3A%20%22%20%22%7D%0Aend%0A%0Agroup%20%23green%20Logout%20%23white%0AClient%20-%3E%20Server%3A%20%5BDELETE%5D%20%2Fsession%5CnauthToken%0AServer%20-%3E%20Handler%3A%20authToken%0AHandler%20-%3E%20Service%3A%20logout(authToken)%0AService%20-%3E%20DataAccess%3A%20deleteAuth(authToken)%0ADataAccess%20-%3E%20db%3A%20Delete%20AuthData%0AService%20--%3E%20Handler%3A%20null%0AHandler%20--%3E%20Server%3A%20null%0AServer%20--%3E%20Client%3A%20200%5Cn%7B%7D%0Aend%0A%0Agroup%20%23red%20List%20Games%20%23white%0AClient%20-%3E%20Server%3A%20%5BGET%5D%20%2Fgame%5CnauthToken%0AServer%20-%3E%20Handler%3AauthToken%0AHandler%20-%3E%20Service%3A%20listGames(ListGameRequest)%0AService%20-%3E%20DataAccess%3A%20getAuth(authToken)%0ADataAccess%20-%3E%20db%3A%20Find%20AuthData%20by%20authToken%0ADataAccess%20--%3E%20Service%3A%20AuthData%0AService%20-%3E%20DataAccess%3A%20getUser(username)%0ADataAccess%20-%3E%20db%3A%20Find%20UserData%20by%20username%0ADataAccess%20--%3E%20Service%3A%20UserData%0AService%20-%3E%20DataAccess%3A%20listGames(username)%0ADataAccess%20-%3E%20db%3A%20Find%20GameData%20by%20username%0ADataAccess%20--%3E%20Service%3A%20GameData%0AService%20--%3E%20Handler%3A%20ListGameResult%0AHandler%20--%3E%20Server%3A%20gameID%2C%20whiteUsername%2C%20blackUsername%2C%20%5CngameName%2C%20game%0AServer%20--%3E%20Client%3A%20200%5Cn%7B%20%22games%22%3A%20%5B%7B%22gameID%22%3A%201234%2C%20%22whiteUsername%22%3A%22%5Cn%22%2C%20%22blackUsername%22%3A%22%22%2C%20%22gameName%3A%22%22%7D%20%5D%7D%0Aend%0A%0Agroup%20%23purple%20Create%20Game%20%23white%0AClient%20-%3E%20Server%3A%20%5BPOST%5D%20%2Fgame%5CnauthToken%5Cn%7BgameName%7D%0AServer%20-%3E%20Handler%3AauthToken%2C%7BgameName%7D%0AHandler%20-%3E%20Service%3A%20createGame(NewGameRequest)%0AService%20-%3E%20DataAccess%3A%20getAuth(authToken)%0ADataAccess%20-%3E%20db%3A%20Find%20AuthData%20by%20authToken%0ADataAccess%20--%3E%20Service%3A%20AuthData%0AService%20-%3E%20DataAccess%3A%20getUser(username)%0ADataAccess%20-%3E%20db%3A%20Find%20UserData%20by%20username%0ADataAccess%20--%3E%20Service%3A%20UserData%0AService%20-%3E%20DataAccess%3A%20createGame(gameName)%0ADataAccess%20-%3E%20db%3A%20Create%20GameData%20with%20gameName%0ADataAccess%20--%3E%20Service%3A%20GameData%0AService%20--%3E%20Handler%3A%20NewGameResult%0AHandler%20--%3E%20Server%3A%20gameID%0AServer%20--%3E%20Client%3A%20200%5Cn%7B%20%22gameID%22%3A%201234%20%7D%0Aend%0A%0Agroup%20%23yellow%20Join%20Game%20%23black%0AClient%20-%3E%20Server%3A%20%5BPUT%5D%20%2Fgame%5CnauthToken%5Cn%7BplayerColor%2C%20gameID%7D%0AServer%20-%3E%20Handler%3AauthToken%5Cn%7BplayerColor%2C%20gameID%7D%0AHandler%20-%3E%20Service%3A%20joinGame(JoinGameRequest)%0AService%20-%3E%20DataAccess%3A%20getAuth(authToken)%0ADataAccess%20-%3E%20db%3A%20Find%20AuthData%20by%20authToken%0ADataAccess%20--%3E%20Service%3A%20AuthData%0AService%20-%3E%20DataAccess%3A%20getUser(username)%0ADataAccess%20-%3E%20db%3A%20Find%20UserData%20by%20username%0ADataAccess%20--%3E%20Service%3A%20UserData%0AService%20-%3E%20DataAccess%3A%20getGame(gameID)%0ADataAccess%20-%3E%20db%3A%20Find%20GameData%20by%20gameID%0ADataAccess%20--%3E%20Service%3A%20GameData%0AService%20-%3E%20DataAccess%3A%20updateGame(gameID)%0ADataAccess%20-%3E%20db%3A%20Update%20GameData%20(whiteUsername%20or%20%5CnblackUsername)%20with%20playercolor%0AService%20--%3E%20Handler%3A%20JoinGameResult%0AHandler%20--%3E%20Server%3A%20null%0AServer%20--%3E%20Client%3A%20200%5Cn%7B%7D%0Aend%0A%0Agroup%20%23gray%20Clear%20application%20%23white%0AClient%20-%3E%20Server%3A%20%5BDELETE%5D%20%2Fdb%0AServer%20-%3E%20Handler%3A%20null%0AHandler%20-%3E%20Service%3A%20clear()%0AService%20-%3E%20DataAccess%3A%20clear()%0ADataAccess%20-%3E%20db%3A%20Delete%20UserData%5CnDelete%20GameDate%5CnDelete%20AuthData%0AService%20--%3E%20Handler%3A%20null%0AHandler%20--%3E%20Server%3A%20null%0AServer%20--%3E%20Client%3A%20200%5Cn%7B%7D%0Aend%0A)

(https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9K+KDvvorxLAC5wFrKaooOUCAHjysL7oeqLorE2IJoYLphm6ZIUrG-rTrO6AmkS4YWhyMDcry06CsKMAEcRaChmRbqdph5QEfGcpYQSOEsuUnrekGAZBiGrrSpGlHRgxYnaI6zrJpBJbITy2a5pg-4gtBJSlMOtyjguk59ERjbNuOrZ9N+V7aYU2Q9jA-aDr0AzloZFnVlOQZMUZVlmJwq7eH4gReCg6B7gevjMMe6SZJg9kXkU1DXtIACiu6pfUqXNC0D6qE+3SmXO7a-mcQIlkV6CacppUcTA8H2FFol1mZaAYTxGr8aSRgoNwmQEc1M6taRTJsVJ5TSL1FKGFx8mYZ2WkKkqKpaexNlLcqJWdglYB9gOQ5LgFnhBRukK2ru0IwAA4qOrIxae8XnswOnXldmU5fYo6Fd5rVbUp5VlJVzGrTp4L1dCN2jKoSEQ6OqEYu1MGdaxAkwOSYADUDI1mhGhSWlRPIxnJ8hCmEQMsaNkmg-KslxnNHXYSj3XowatJuaM2PkeNBO8gadEiuzYgSeaSaXjB5R8-TSOM5T3XILEkNqGzI4cxTOMUeU1G2or-MwEBatc9TsHXaO3HS-9qblBdCtw2oGkgy9aafaMACS0izCemQGncsw6AgoANt7o6zN0zsoAAcsHaOjn9pw7Xtzk9FMYeqOMlT9GHbtpwAjL2ADMAAsTye-qgsrN0Xx+wHQejCsXxh5HowTFZMCNIdK7HeugTYD4UDYNw8C6pkJujCksVnjkz1i7plS1A0H1fcEP1zkODejtZyWlYtQPTD0a+jBBANsnVQmZIrsLhygySK-D6EKYmfFM+6aP4SJWMG2NeOUVrtPBtopMwHJsLCMRtOLE2APfXiMgurPxZjSICnNP7sk1oTO0Ap-70X1sAta4s0FQDNk6B+0Cn6CSHigc++8UCIMkl-cocAyEjxQIKZIGRUjR1GI3IWMCcGEONjrKWvCFo1XKJfa+tscwIDzDVHhJZM7SFjnZJ6CchzJ1HFncoudC4wHboFLuARLC9XgskGAAApCAPJGGBCriABsj1J7H3WrPSkd4Whh2+i1FevR+7AAMVAOAEB4JQFmHIjexwLbAlKEDIc3jfH+MCcEtR0hD6pgcbggAVuYtA58zE8hvigNECNIHI1lrA1+fpBpMWoSLWhPMiZ0xJvRIB3DRZJV4WA+pED5oyxxnhKk8DBZVNxsg2peDdZYOaaA0ZAjnSPxKb0ihiTBkaxGfwhpYQ5EfyptPMGqzOkM1mT0vW2AtBn1HLCORSzuaUmOXqRhgpYQlxroYaAMBujWMDgMmALC-QwBiZQEAAToBFJaYtUxmS8kSKkUfFpP4Noqh-NtJRjl9q9B0Z3YKAQvA+K7F6WAwBsD90IPERIY8Ho7VSTPCoaUMpZRysYP6f5hEwEVJtEG2yaYgG4HgWEiNBHdPDIJLlcJLk1Mmn1Qwkt5DdDFdNO5eppVTWHrNeQwLwklhZStaRMLHEaoUV2SeyjUX+RXEAA)

Default view
(https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9K+KDvvorxLAC5wFrKaooOUCAHjysL7oeqLorE2IJoYLphm6ZIUrG-rTrO6AmkS4YWhyMDcry06CsKMAEcRaChmRbqdph5QEfGcpYQSOEsuUnrekGAZBiGrrSpGlHRgxYnaI6zrJpBJbITy2a5pg-4gtBJSlMOtyjguk59ERjbNuOrZ9N+V7aYU2Q9jA-aDr0AzloZFnVlOQZMUZVlmJwq7eH4gReCg6B7gevjMMe6SZJg9kXkU1DXtIACiu6pfUqXNC0D6qE+3SmXO7a-mcQIlkV6CacppUcTA8H2FFol1mZaAYTxGr8aSRgoNwmQEc1M6taRTJsVJ5TSL1FKGFx8mYZ2WkKkqKpaexNlLcqJWdglYB9gOQ5LgFnhBRukK2ru0IwAA4qOrIxae8XnswOnXldmU5fYo6Fd5rVbUp5VlJVzGrTp4L1dCN2jKoSEQ6OqEYu1MGdaxAkwOSYADUDI1mhGhSWlRPIxnJ8hCmEQMsaNkmg-KslxnNHXYSj3XowatJuaM2PkeNBO8gadEiuzYgSeaSaXjB5R8-TSOM5T3XILEkNqGzI4cxTOMUeU1G2or-MwEBatc9TsHXaO3HS-9qblBdCtw2oGkgy9aafaMACS0izCemQGncsw6AgoANt7o6zN0zsoAAcsHaOjn9pw7Xtzk9FMYeqOMlT9GHbtpwAjL2ADMAAsTye-qgsrN0Xx+wHQejCsXxh5HowTFZMCNIdK7HeugTYD4UDYNw8C6pkJujCksVnjkz1i7plS1A0H1fcEP1zkODejtZyWlYtQPTD0a+jBBANsnVQmZIrsLhygySK-D6EKYmfFM+6aP4SJWMG2NeOUVrtPBtopMwHJsLCMRtOLE2APfXiMgurPxZjSICnNP7sk1oTO0Ap-70X1sAta4s0FQDNk6B+0Cn6CSHigc++8UCIMkl-cocAyEjxQIKZIGRUjR1GI3IWMCcGEONjrKWvCFo1XKJfa+tscwIDzDVHhJZM7SFjnZJ6CchzJ1HFncoudC4wHboFLuARLC9XgskGAAApCAPJGGBCriABsj1J7H3WrPSkd4Whh2+i1FevR+7AAMVAOAEB4JQFmHIjexwLbAlKEDIc3jfH+MCcEtR0hD6pgcbggAVuYtA58zE8hvigNECNIHI1lrA1+fpBpMWoSLWhPMiZ0xJvRIB3DRZJV4WA+pED5oyxxnhKk8DBZVNxsg2peDdZYOaaA0ZAjnSPxKb0ihiTBkaxGfwhpYQ5EfyptPMGqzOkM1mT0vW2AtBn1HLCORSzuaUmOXqRhgpYQlxroYaAMBujWMDgMmALC-QwBiZQEAAToBFJaYtUxmS8kSKkUfFpP4Noqh-NtJRjl9q9B0Z3YKAQvA+K7F6WAwBsD90IPERIY8Ho7VSTPCoaUMpZRysYP6f5hEwEVJtEG2yaYgG4HgWEiNBHdPDIJLlcJLk1Mmn1Qwkt5DdDFdNO5eppVTWHrNeQwLwklhZStaRMLHEaoUV2SeyjUX+RXEAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
