package it.polimi.ingsw.network.client;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientUserCommunication implements Runnable {
    private final BufferedReader stdin;
    private final Client client;

    public ClientUserCommunication(Client client, BufferedReader stdin) {
        this.stdin = stdin;
        this.client = client;
    }

    @Override
    public void run() {
        String line;
        try {
            while(!Thread.interrupted() && (line = stdin.readLine()) != null) {
                client.handleUserMessage(line);
            }
        } catch (IOException e) {
        }
    }
}
