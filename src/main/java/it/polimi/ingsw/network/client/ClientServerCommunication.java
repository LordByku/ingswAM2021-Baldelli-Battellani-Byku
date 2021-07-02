package it.polimi.ingsw.network.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;

public class ClientServerCommunication implements Runnable {
    private final BufferedReader in;
    private final Client client;
    private final int timerDelay = 10000;

    public ClientServerCommunication(Client client, Socket socket) throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.client = client;
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = in.readLine()) != null) {
                System.out.println("Client received: " + line);
                client.handleServerMessage(line);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Socket timeout exception");
            client.reconnect();
        } catch (IOException e) {
            if(Thread.interrupted()) {
                System.out.println("Thread interrupted");
                return;
            } else {
                System.out.println("Unexpected io exception");
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        client.reconnect();
                    }
                }, 2 * timerDelay);
            }
        }
    }
}
