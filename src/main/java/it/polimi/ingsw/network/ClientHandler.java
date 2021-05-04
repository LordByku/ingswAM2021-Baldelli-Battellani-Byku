package it.polimi.ingsw.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game.*;

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
    private String nickname;

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
            Game.getInstance().removePlayer(nickname);
        } catch (GameAlreadyStartedException e) {
            e.printStackTrace();
        }
        server.removeClientHandler(this);
        broadcast("playerList", getJsonPlayerList());
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
            nickname = line;

            System.out.println("Received nickname " + nickname);
            try {
                Game.getInstance().addPlayer(nickname);
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

        System.out.println("Accepted nickname " + nickname);

        broadcast("playerList", getJsonPlayerList());

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

