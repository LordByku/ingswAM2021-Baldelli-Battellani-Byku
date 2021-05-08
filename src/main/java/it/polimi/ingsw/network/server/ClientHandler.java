package it.polimi.ingsw.network.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.network.server.serverStates.AcceptNickname;
import it.polimi.ingsw.network.server.serverStates.ServerState;
import it.polimi.ingsw.parsing.Parser;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private final Scanner in;
    private final PrintWriter out;
    private final Server server;
    private Person person;
    private ServerState serverState;

    public ClientHandler(Socket socket, Server server) throws IOException {
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
        this.server = server;
    }

    public void fatalError(String message) {
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("status", "fatalError");
        jsonMessage.addProperty("message", message);

        out.println(jsonMessage.toString());
    }

    public void error(String message) {
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("status", "error");
        jsonMessage.addProperty("message", message);

        out.println(jsonMessage.toString());
    }

    public void ok(String type, JsonObject message) {
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("status", "ok");
        jsonMessage.addProperty("type", type);
        jsonMessage.add("message", message);

        out.println(jsonMessage.toString());
    }

    public void broadcast(String type, JsonObject message) {
        server.broadcast(type, message);
    }

    public synchronized void connectionClosed() {
        System.out.println("Connection with client has been interrupted");
        try {
            if(person != null) {
                Game.getInstance().removePlayer(person.getNickname());
            }
        } catch (GameAlreadyStartedException e) {
            e.printStackTrace();
        }
        server.removeClientHandler(this);
        broadcast("playerList", ServerParser.getInstance().getJsonPlayerList());
    }

    public Person getPerson(){
        return person;
    }

    public void run() {
        serverState = new AcceptNickname();
        Thread thread = new Thread(new ServerClientCommunication(this, in));
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void handleClientMessage(String line) {
        serverState.handleClientMessage(this, line);
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
}
