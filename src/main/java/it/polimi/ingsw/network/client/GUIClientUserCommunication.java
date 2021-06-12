package it.polimi.ingsw.network.client;

import java.util.concurrent.BlockingQueue;

public class GUIClientUserCommunication implements Runnable {
    private final Client client;
    private final BlockingQueue<String> buffer;

    public GUIClientUserCommunication(Client client, BlockingQueue<String> buffer) {
        this.client = client;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                String line = buffer.take();
                client.handleUserMessage(line);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
