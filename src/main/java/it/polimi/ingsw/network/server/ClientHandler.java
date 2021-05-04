package it.polimi.ingsw.network.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game.*;
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

    public ClientHandler(Socket socket, Server server) throws IOException {
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);
        this.server = server;
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

    public void sendOkMessage(JsonObject message){
        ok("update", message);
    }

    private JsonObject getJsonPlayerList() {
        ArrayList<Player> players = Game.getInstance().getPlayers();
        JsonObject jsonObject = new JsonObject();
        JsonArray playerList = new JsonArray();

        for(Player player: players) {
            Person person = (Person) player;

            JsonObject playerObject = new JsonObject();
            playerObject.addProperty("nickname", person.getNickname());
            playerObject.addProperty("isHost", person.isHost());

            playerList.add(playerObject);
        }

        jsonObject.add("playerList", playerList);

        return jsonObject;
    }

    private void connectionClosed() {
        System.out.println("Connection with client has been interrupted");
        try {
            Game.getInstance().removePlayer(person.getNickname());
        } catch (GameAlreadyStartedException e) {
            e.printStackTrace();
        }
        server.removeClientHandler(this);
        broadcast("playerList", getJsonPlayerList());
    }

    public Person getPerson(){
        return person;
    }

    public void run() {
        String line;
        boolean validNickname = false;

        do {
            try {
                line = in.nextLine();
            } catch (NoSuchElementException e) {
                connectionClosed();
                return;
            }
            String nickname = line;

            System.out.println("Received nickname " + nickname);
            try {
                person = (Person) Game.getInstance().addPlayer(nickname);
                validNickname = true;
            } catch (FullLobbyException e) {
                error("Lobby is already full");
                e.printStackTrace();
            } catch (ExistingNicknameException e) {
                error("The nickname " + nickname + " has already been taken");
                e.printStackTrace();
            } catch (InvalidNicknameException e) {
                error("The nickname is not valid");
                e.printStackTrace();
            }
        } while(!validNickname);

        System.out.println("Accepted nickname " + person.getNickname());

        broadcast("playerList", getJsonPlayerList());

        try {
            line = in.nextLine();
            if(person.isHost()){
                if (Game.getInstance().getNumberOfPlayers() >= 2 && line.equals("start")) {
                    JsonObject jsonMessage = new JsonObject();
                    JsonArray playerOrder = new JsonArray();
                    for (Player player: Game.getInstance().getPlayers()) {
                        playerOrder.add(((Person) player).getNickname());
                    }
                    JsonObject config = Parser.getInstance().getConfig();
                    jsonMessage.add("config", config);
                    jsonMessage.add("turnOrder:", playerOrder);
                    broadcast("config", jsonMessage);

                    Game.getInstance().start();

                    server.initialGameState();
                }
            }
            else{
                error("Not the host");
            }
        } catch (NoSuchElementException e) {
            connectionClosed();
            return;
        } catch (GameAlreadyStartedException e) {
            e.printStackTrace();
        }
        while(true) {
            try {
                line = in.nextLine();
            } catch (NoSuchElementException e) {
                connectionClosed();
                return;
            }
        }
    }
}

