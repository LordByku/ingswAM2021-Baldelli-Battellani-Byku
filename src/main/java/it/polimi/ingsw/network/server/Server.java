package it.polimi.ingsw.network.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.Person;
import it.polimi.ingsw.model.game.Player;
import it.polimi.ingsw.model.game.PlayerType;
import it.polimi.ingsw.network.server.serverStates.GameStarted;
import it.polimi.ingsw.utility.JsonUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Server {
    private final int port;
    private final ArrayList<ClientHandler> clientHandlers;

    public Server(int port) {
        this.port = port;
        clientHandlers = new ArrayList<>();
    }

    public void start() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Server ready");

        while (!Game.getInstance().hasGameEnded()) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);
                synchronized (clientHandlers) {
                    clientHandlers.add(clientHandler);
                }
                executor.submit(clientHandler);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        executor.shutdown();
    }

    public void broadcast(String type, JsonObject message) {
        synchronized (clientHandlers) {
            for (ClientHandler clientHandler : clientHandlers) {
                System.out.println("broadcasting to " + clientHandler);
                clientHandler.ok(type, message);
            }
        }
    }

    public void startGame() {
        synchronized (clientHandlers) {
            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.sendGameState();
                clientHandler.setState(new GameStarted());
                sendAllCommandBuffers(clientHandler);
            }
        }
    }

    public void endGame() {
        synchronized (clientHandlers) {
            JsonObject endGameMessage = buildEndGameMessage();
            for(ClientHandler clientHandler: clientHandlers) {
                clientHandler.ok("endGame", endGameMessage);
            }
        }
    }

    public void removeClientHandler(ClientHandler clientHandler) {
        synchronized (clientHandlers) {
            clientHandlers.remove(clientHandler);
        }
    }

    public void updateGameState(Consumer<GameStateSerializer> lambda) {
        synchronized (clientHandlers) {
            for (ClientHandler clientHandler : clientHandlers) {
                GameStateSerializer serializer = new GameStateSerializer(clientHandler.getPerson().getNickname());
                lambda.accept(serializer);
                clientHandler.ok("update", serializer.getMessage());
            }
        }
    }

    public void updateGameState(Consumer<GameStateSerializer> lambda, ClientHandler excluded) {
        synchronized (clientHandlers) {
            for (ClientHandler clientHandler : clientHandlers) {
                if (!clientHandler.equals(excluded)) {
                    GameStateSerializer serializer = new GameStateSerializer(clientHandler.getPerson().getNickname());
                    lambda.accept(serializer);
                    clientHandler.ok("update", serializer.getMessage());
                }
            }
        }
    }

    public void sendAllCommandBuffers(ClientHandler sendingClientHandler) {
        synchronized (clientHandlers) {
            for (ClientHandler clientHandler : clientHandlers) {
                JsonObject commandObject = JsonUtil.getInstance().serializeCommandBuffer(clientHandler.getCommandBuffer(), clientHandler.getPerson());
                sendingClientHandler.ok("command", commandObject);
            }
        }
    }

    public JsonObject buildEndGameMessage() {
        JsonObject message = new JsonObject();

        JsonArray jsonArray = new JsonArray();
        ArrayList<Player> players = Game.getInstance().getPlayers();
        for(Player player: players) {
            if(player.getPlayerType() == PlayerType.PERSON) {
                Person person = (Person) player;
                JsonObject playerObject = new JsonObject();
                playerObject.addProperty("player", person.getNickname());
                playerObject.addProperty("basePoints", person.getBoard().getPoints());
                playerObject.addProperty("resources", person.getBoard().getResources().size());

                jsonArray.add(playerObject);
            }
        }

        message.add("results", jsonArray);
        if(Game.getInstance().getNumberOfPlayers() == 1) {
            message.addProperty("computerWin", Game.getInstance().getComputer().hasWon());
        }

        return message;
    }
}
