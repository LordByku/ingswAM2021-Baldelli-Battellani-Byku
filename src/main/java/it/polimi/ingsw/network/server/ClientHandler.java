package it.polimi.ingsw.network.server;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.network.server.serverStates.AcceptNickname;
import it.polimi.ingsw.network.server.serverStates.ServerState;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ClientHandler implements Runnable {
    private final Scanner in;
    private final PrintWriter out;
    private final Server server;
    private Person person;
    private ServerState serverState;
    private Timer timer;
    private Thread serverClientCommunication;
    private final int timerDelay = 10000;
    private volatile boolean ponged;

    public ClientHandler(Socket socket, Server server) throws IOException {
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
        this.server = server;
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

    public synchronized void confirm() {
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("status", "ok");
        jsonMessage.addProperty("type", "confirm");
        out.println(jsonMessage.toString());
    }

    public synchronized void ping() {
        System.out.println(new Timestamp(new Date().getTime()) + " Sending ping from client handler " + this);
        out.println("ping");
    }

    public void broadcast(String type, JsonObject message) {
        server.broadcast(type, message);
    }

    public void sendGameState() {
        GameStateSerializer serializer = new GameStateSerializer(getPerson().getNickname());
        ok("update", serializer.gameState());
    }

    public synchronized void disconnection() {
        timer.cancel();
        if(serverClientCommunication.isAlive()) {
            serverClientCommunication.interrupt();
        }
        System.out.println("Connection with client has been interrupted");
        try {
            // person may be null if an exception is thrown trying to add the person to the game
            if(person != null) {
                Game.getInstance().removePlayer(person.getNickname());
                server.removeClientHandler(this);
                broadcast("playerList", ServerParser.getInstance().getJsonPlayerList());
            }
        } catch (GameAlreadyStartedException e) {
            person.disconnect();
        }
    }

    public synchronized void handlePing() {
        if(ponged) {
            ponged = false;
            timer = new Timer();
            ping();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handlePing();
                }
            }, timerDelay);
        } else {
            disconnection();
        }
    }

    public Person getPerson(){
        return person;
    }

    public void run() {
        serverState = new AcceptNickname();
        serverClientCommunication = new Thread(new ServerClientCommunication(this, in));
        serverClientCommunication.start();

        ponged = false;
        timer = new Timer();
        ping();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handlePing();
            }
        }, timerDelay);

        try {
            serverClientCommunication.join();
            System.out.println("ServerClientCommunication closed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void handleClientMessage(String line) {
        if(line.equals("pong")) {
            System.out.println(new Timestamp(new Date().getTime()) + " Received pong by client handler " + this);
            ponged = true;
        } else {
            serverState.handleClientMessage(this, line);
        }
    }

    public void setPlayer(Person person) {
        this.person = person;
    }

    public void setState(ServerState serverState) {
        this.serverState = serverState;
    }

    public void startGame() {
        server.startGame();
    }

    public void updateGameState(Consumer<GameStateSerializer> lambda) {
        server.updateGameState(lambda);
    }


    public void advanceAllStates(Supplier<ServerState> serverStateSupplier) {
        server.advanceAllStates(serverStateSupplier);
    }
}

