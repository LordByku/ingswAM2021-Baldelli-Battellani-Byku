package it.polimi.ingsw.network.server;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerClientCommunication implements Runnable {
    private final ClientHandler clientHandler;
    private final BufferedReader in;

    public ServerClientCommunication(ClientHandler clientHandler, BufferedReader in) {
        this.clientHandler = clientHandler;
        this.in = in;
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = in.readLine()) != null) {
                clientHandler.handleClientMessage(line);
            }
        } catch (IOException e) {
        } finally {
            clientHandler.disconnection();
        }
    }
}
