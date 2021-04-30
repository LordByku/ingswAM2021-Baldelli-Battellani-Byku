package it.polimi.ingsw.network;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.game.ExistingNicknameException;
import it.polimi.ingsw.model.game.FullLobbyException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.InvalidNicknameException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void error(PrintWriter out, String message) {
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("status", "error");
        jsonMessage.addProperty("message", message);

        out.println(jsonMessage.toString());
    }

    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String line;
            boolean validNickname = false;

            do {
                line = in.nextLine();
                String nickname = line;

                System.out.println("Received nickname " + nickname);
                try {
                    Game.getInstance().addPlayer(nickname);
                    System.out.println("Accepted nickname " + nickname);
                    validNickname = true;
                } catch (FullLobbyException e) {
                    error(out, "Lobby is already full");
                    e.printStackTrace();
                } catch (ExistingNicknameException e) {
                    error(out, "The nickname " + nickname + " has already been taken");
                    e.printStackTrace();
                } catch (InvalidNicknameException e) {
                    error(out, "The nickname is not valid");
                    e.printStackTrace();
                }
            } while(!validNickname);

            // TODO out.println("nickname ok");
            //out.flush();

            while(true) {
                line = in.nextLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
