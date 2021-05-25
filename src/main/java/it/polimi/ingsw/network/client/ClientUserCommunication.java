package it.polimi.ingsw.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientUserCommunication implements Runnable {
    private final BufferedReader stdin;
    private final Client client;

    public ClientUserCommunication(Client client) {
        stdin = new BufferedReader(new InputStreamReader(System.in));
        this.client = client;
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = stdin.readLine()) != null) {
                client.handleUserMessage(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
