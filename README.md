# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

[![Sequence Diagram](Phase 2.png)]
https://sequencediagram.org/index.html#initialData=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

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
