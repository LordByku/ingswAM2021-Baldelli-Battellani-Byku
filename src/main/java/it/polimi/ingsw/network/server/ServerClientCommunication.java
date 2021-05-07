package it.polimi.ingsw.network.server;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerClientCommunication implements Runnable {
    private final ClientHandler clientHandler;
    private final Scanner in;

    public ServerClientCommunication(ClientHandler clientHandler, Scanner in) {
        this.clientHandler = clientHandler;
        this.in = in;
    }

    @Override
    public void run() {
        String line;
        try {
            while((line = in.nextLine()) != null) {
                clientHandler.handleClientMessage(line);
            }
        } catch (NoSuchElementException e) {
            clientHandler.connectionClosed();
        }
    }
}
