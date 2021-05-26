package it.polimi.ingsw.network.server;

import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.CommandBuffer;
import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.network.server.serverStates.AcceptNickname;
import it.polimi.ingsw.network.server.serverStates.ServerState;
import it.polimi.ingsw.utility.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final Server server;
    private final int timerDelay = 10000;
    private Person person;
    private ServerState serverState;
    private Timer timer;
    private volatile boolean ponged;
    private CommandBuffer commandBuffer;

    public ClientHandler(Socket socket, Server server) throws IOException {
        socket.setSoTimeout(2 * timerDelay);
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.server = server;
        commandBuffer = null;
    }

    public synchronized void fatalError(String message) {
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("status", "fatalError");
        jsonMessage.addProperty("message", message);

        out.println(jsonMessage.toString());
    }

    public synchronized void error(String message) {
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("status", "error");
        jsonMessage.addProperty("message", message);

        out.println(jsonMessage.toString());
    }

    public synchronized void ok(String type, JsonObject message) {
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("status", "ok");
        jsonMessage.addProperty("type", type);
        jsonMessage.add("message", message);

        out.println(jsonMessage.toString());
    }

    public synchronized void ping() {
        out.println("ping");
    }

    public void broadcast(String type, JsonObject message) {
        System.out.println(this + " requesting broadcast");
        server.broadcast(type, message);
    }

    public void sendGameState() {
        GameStateSerializer serializer = new GameStateSerializer(getPerson().getNickname());
        ok("update", serializer.gameState());
    }

    public synchronized void disconnection() {
        System.out.println("Client has disconnected");
        server.removeClientHandler(this);
        try {
            // person may be null if an exception is thrown trying to add the person to the game
            if (person != null) {
                Game.getInstance().removePlayer(person.getNickname());
                broadcast("playerList", GameStateSerializer.getJsonPlayerList());
            }
        } catch (GameAlreadyStartedException e) {
            if(!Game.getInstance().hasGameEnded()) {
                person.disconnect();
                if (commandBuffer != null) {
                    Consumer<GameStateSerializer> lambda = commandBuffer.kill();
                    if (lambda != null) {
                        updateGameState(lambda);
                    }
                }
                endTurn();
            }
        }
    }

    public synchronized void handlePing() {
        if (ponged) {
            ponged = false;
            timer = new Timer();
            ping();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handlePing();
                }
            }, timerDelay);
        }
    }

    public synchronized Person getPerson() {
        while (person == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return person;
    }

    public void run() {
        serverState = new AcceptNickname();
        Thread serverClientCommunication = new Thread(new ServerClientCommunication(this, in));
        serverClientCommunication.start();

        ponged = true;
        handlePing();

        try {
            serverClientCommunication.join();
            if (!socket.isClosed()) {
                socket.close();
            }
            System.out.println("ServerClientCommunication closed");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public void handleClientMessage(String line) {
        System.out.println(this + " received " + line);
        if ("pong".equals(line)) {
            ponged = true;
        } else {
            if(Game.getInstance().hasGameEnded()) {
                ok("endGame", server.buildEndGameMessage());
            } else {
                serverState.handleClientMessage(this, line);
            }
        }
    }

    public synchronized void setPlayer(Person person) {
        this.person = person;
        notifyAll();
    }

    public void setState(ServerState serverState) {
        this.serverState = serverState;
    }

    public void startGame() {
        server.startGame();
    }

    public void endGame() {
        server.endGame();
    }

    public void updateGameState(Consumer<GameStateSerializer> lambda) {
        server.updateGameState(lambda);
    }

    public void updateGameState(Consumer<GameStateSerializer> lambda, ClientHandler excluded) {
        server.updateGameState(lambda, excluded);
    }

    public void sendAllCommandBuffers() {
        server.sendAllCommandBuffers(this);
    }

    public CommandBuffer getCommandBuffer() {
        return commandBuffer;
    }

    public void setBuffer(CommandBuffer commandBuffer) {
        this.commandBuffer = commandBuffer;
    }

    public void endTurn() {
        if (person.isActivePlayer()) {
            person.endTurn();
        }
        if(Game.getInstance().hasGameEnded()) {
            endGame();
        } else {
            Consumer<GameStateSerializer> lambda = (serializer) -> {
                for (Player player : Game.getInstance().getPlayers()) {
                    if (player.getPlayerType() == PlayerType.PERSON) {
                        serializer.addPlayerDetails((Person) player);
                    }
                }
            };
            updateGameState(lambda);

            broadcast("command", JsonUtil.getInstance().serializeCommandBuffer(commandBuffer, person));
        }
    }
}

