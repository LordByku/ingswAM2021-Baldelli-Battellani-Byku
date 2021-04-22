package it.polimi.ingsw.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private final String hostname;
    private final int port;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void start() {
        try {
            Socket socket = new Socket(hostname, port);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

            String userInput;

            while((userInput = stdin.readLine()) != null) {
                // TODO
            }
        } catch (UnknownHostException e) {
            System.out.println("Invalid hostname: " + hostname);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("I/O error with " + hostname);
            e.printStackTrace();
        }
    }
}
