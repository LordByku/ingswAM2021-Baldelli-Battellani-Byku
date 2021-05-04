package it.polimi.ingsw.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientServerCommunication implements Runnable {
    private final BufferedReader in;
    private final Client client;

    public ClientServerCommunication(Client client, Socket socket) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.client = client;
    }
    @Override
    public void run() {
        String line;
        try {
            while((line = in.readLine()) != null) {
                client.handleServerMessage(line);
            }
        } catch (IOException e) {
            return;
        }
    }
}
