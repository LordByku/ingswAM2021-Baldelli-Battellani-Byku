package it.polimi.ingsw.network.client;

import java.util.concurrent.BlockingQueue;

public class LocalClientCommunication implements Runnable {
    private final Client client;
    private final BlockingQueue<String> readBuffer;

    public LocalClientCommunication(Client client, BlockingQueue<String> readBuffer) {
        this.client = client;
        this.readBuffer = readBuffer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                client.handleServerMessage(readBuffer.take());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
